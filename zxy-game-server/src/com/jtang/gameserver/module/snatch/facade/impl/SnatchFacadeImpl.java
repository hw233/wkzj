package com.jtang.gameserver.module.snatch.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.EXCHANGE_FAIL_SCORE_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.SNATCH_CONFIG_NOT_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.SNATCH_TIME_UNOPENED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.event.EventBus;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.SnatchResultEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.dataconfig.model.SnatchAchieveConfig;
import com.jtang.gameserver.dataconfig.model.SnatchConfig;
import com.jtang.gameserver.dataconfig.model.SnatchExchangeConfig;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dataconfig.service.SnatchExchangeService;
import com.jtang.gameserver.dataconfig.service.SnatchService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Snatch;
import com.jtang.gameserver.module.battle.constant.BattleSkipPlayType;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackPlayerRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.snatch.constant.SnatchRule;
import com.jtang.gameserver.module.snatch.dao.SnatchDao;
import com.jtang.gameserver.module.snatch.facade.SnatchEnemyFacade;
import com.jtang.gameserver.module.snatch.facade.SnatchFacade;
import com.jtang.gameserver.module.snatch.facade.SnatchRobotFacade;
import com.jtang.gameserver.module.snatch.handler.response.SnatchNumResponse;
import com.jtang.gameserver.module.snatch.helper.SnatchHelper;
import com.jtang.gameserver.module.snatch.helper.SnatchPushHelper;
import com.jtang.gameserver.module.snatch.model.SnatchVO;
import com.jtang.gameserver.module.snatch.result.impl.GoldSnatchResultImpl;
import com.jtang.gameserver.module.snatch.type.ExchangeType;
import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class SnatchFacadeImpl implements SnatchFacade, BattleCallBack,ActorLoginListener,ZeroListener,ApplicationListener<ContextRefreshedEvent> {
	private final Logger LOGGER = LoggerFactory.getLogger(SnatchFacadeImpl.class);
	//抢夺者的角色等级
	static String SNATCH_ACTOR_LEVEL = "SNATCH_ACTOR_LEVEL";
	//抢夺目标的actorId
	static String SNATCH_TARGET_ACTORID = "SNATCH_TARGET_ACTORID"; 
	//抢夺目标的actorName
	static String SNATCH_TARGET_ACTOR_NAME = "SNATCH_TARGET_ACTOR_NAME";
	//抢夺目标的等级
	static String SNATCH_TARGET_LEVEL = "SNATCH_TARGET_LEVEL";
	//抢夺目标的类型 
	static String SNATCH_TARGET_TYPE = "SNATCH_TARGET_TYPE";
	//抢夺配置
	static String SNATCH_CONFIG = "SNATCH_CONFIG";
	//通知自增id
	static String NOTIFY_ID = "NOTIFY_ID";
	//消耗的精力
	static String ENERGY_NUM = "ENERGY_NUM";
	
	@Autowired
	SnatchDao snatchDao;
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	HeroSoulFacade heroSoulFacade;
	@Autowired
	GoodsFacade goodsFacade;
	@Autowired
	BattleFacade battleFacade;
	@Autowired
	EquipFacade equipFacade;
	@Autowired
	NotifyFacade notifyFacade;
	@Autowired
	GoldSnatchResultImpl goldSnatchResultImpl;
	@Autowired
	SnatchEnemyFacade snatchEnemyFacade;
	@Autowired
	EventBus eventBus;	
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private Schedule schedule;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private SnatchRobotFacade snatchRobotFacade;

	@Override
	public Snatch get(long actorId) {
		return snatchDao.get(actorId);
	}
		
	@Override
	public Result startSnatch(long actorId, long targetActorId, long notifyId) {
		SnatchEnemyType enemyType = SnatchEnemyType.ACTOR;
		if (actorFacade.getActor(targetActorId) == null) {
			enemyType = SnatchEnemyType.ROBOT;
		}

		SnatchConfig config = SnatchService.get();
		if(config == null) {
			return Result.valueOf(SNATCH_CONFIG_NOT_EXISTS);
		} 
		
		int hour = TimeUtils.getHour();
		if (hour < config.getOpenTime() || hour > config.getEndTime()) {
			return Result.valueOf(SNATCH_TIME_UNOPENED);
		}
		
		Result checkResult = goldSnatchResultImpl.checkSnatch(actorId, targetActorId, enemyType, config);
		if (checkResult.isFail()) {
			return checkResult;
		}
		
		//获取双方战斗英雄
		LineupFightModel atkLineup = LineupHelper.getLineupFight(actorId);
		LineupFightModel defLineup = SnatchHelper.getTargetLineUp(targetActorId, enemyType);
		

		Actor actor = actorFacade.getActor(actorId);
		int targetLevel = SnatchHelper.getTargetLevel(targetActorId, enemyType);
		int energyNum = config.getConsumeEnergy(targetLevel);

		//抢夺开始前判断精力是否足够
		Snatch snatch = snatchDao.get(actorId);
		
		if(snatch.snatchNum == config.snatchMaxNum){//当角色战斗次数满值,开始战斗才进行恢复计时。
			snatch.flushFightTime = TimeUtils.getNow();
		}
		
		//设置战斗请求参数
		MapConfig map = MapService.get(config.getMapId());
		Map<String, Object> args = new HashMap<String, Object>();
		args.put(SNATCH_ACTOR_LEVEL, actor.level);
		args.put(SNATCH_TARGET_ACTORID, targetActorId);
		args.put(SNATCH_TARGET_ACTOR_NAME, SnatchHelper.getTargetActorName(targetActorId));
		args.put(SNATCH_TARGET_LEVEL, targetLevel);
		args.put(SNATCH_TARGET_TYPE, enemyType);
		args.put(SNATCH_CONFIG, config);
		args.put(NOTIFY_ID, notifyId);
		args.put(ENERGY_NUM, energyNum);
		
		int targetMorale = SnatchHelper.getTargetMorale(targetActorId, enemyType);		
		
		AttackPlayerRequest event = new AttackPlayerRequest(EventKey.SNATCH_BATTLE, map, actorId, atkLineup, targetActorId, defLineup, actor.morale,
				targetMorale, args, BattleType.SNATCH);
		Result result = battleFacade.submitAtkPlayerRequest(event, this);

		// 战斗没有开打,前期检查没有通过.
		if (result.isFail()) {
			return Result.valueOf(result.statusCode);
		}
		return Result.valueOf();
	}

	@Override
	public void execute(BattleResult result) {
		long actorId = result.battleReq.actorId;

		// 处理出错
		if (result.statusCode != StatusCode.SUCCESS) {
			SnatchPushHelper.pushSnatchFail(actorId, result.statusCode);
			return;
		}

		FightData fightData = result.fightData;
		if (ActorHelper.getActorLevel(actorId) >= SnatchRule.SNATCH_EXCEED_LEVEL_PASS_BATTLE) {
			fightData.setCanSkipPlay(BattleSkipPlayType.PVE_CAN_SKIP);
		} else {
			fightData.setCanSkipPlay(BattleSkipPlayType.PVP_CLIENT_DECIDE);
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> args = (Map<String, Object>) result.battleReq.args;
		int actorLevel = (int) args.get(SNATCH_ACTOR_LEVEL);
		long targetActorId = (long) args.get(SNATCH_TARGET_ACTORID);
		String targetActorName = String.valueOf(args.get(SNATCH_TARGET_ACTOR_NAME));
		int targetLevel = (int) args.get(SNATCH_TARGET_LEVEL);
		SnatchEnemyType enemyType = (SnatchEnemyType) args.get(SNATCH_TARGET_TYPE);
		SnatchConfig config = (SnatchConfig) args.get(SNATCH_CONFIG);
		long notifyId = (long) args.get(NOTIFY_ID);
		int energyNum = (int) args.get(ENERGY_NUM);
		WinLevel winlevel = fightData.result;

		// 战斗结果在execute中发送(为了控制先发送战斗结果，再发送物品等数值对象
		SnatchVO snatchVo = goldSnatchResultImpl.calculateSnatch(actorId, targetActorId, enemyType, winlevel, config);

		// 这里可以处理vip的附赠物品
		List<RewardObject> rewardList = new ArrayList<>();
		
		// 更新当前抢夺者的积分
		Snatch snatch = snatchDao.get(actorId);
		ChainLock lock = LockUtils.getLock(snatch);
		try{
			lock.lock();
			if(snatch.useSnatch >= config.rewardNum){//没达到每日获得奖励上限
				snatchVo.score = 0;
			}
			snatch.lastFightTime = TimeUtils.getNow();
			snatch.snatchNum -= energyNum;
			snatch.useSnatch ++;
			SnatchPushHelper.pushFightNum(actorId,snatch);
			if (snatch.getAchimentMap().isEmpty() == false) {//抢夺成就
				Map<Integer,Integer> achieveMap = new HashMap<>(snatch.getAchimentMap());
				for(Entry<Integer,Integer> entry : achieveMap.entrySet()){
					int achieveId = entry.getKey();
					int fightNum = entry.getValue();
					fightNum += 1;
					SnatchAchieveConfig achieveConfig = SnatchService.getAchieve(achieveId);
					switch(achieveConfig.getAchieveType()){
					case TYPE1:
						if(snatch.useSnatch <= config.rewardNum){//没达到每日获得奖励上限
							snatch.score += snatchVo.score;
							if(achieveConfig.fightNum <= fightNum){//成就已完成,发放奖励,设置下一个成就
								SnatchAchieveConfig nextAchieve = SnatchService.getNextAchieve(achieveId);
								snatch.getAchimentMap().remove(achieveId);
								if(nextAchieve != null){//还有下一个成就
									snatch.getAchimentMap().put(nextAchieve.achieveId, 0);
								}
								int level = ActorHelper.getActorLevel(snatch.getPkId());
								rewardList.addAll(achieveConfig.getRewardList(level));
								RewardObject extReward = achieveConfig.getExeReward(actorLevel);
								if(extReward != null){
									rewardList.add(extReward);
								}
							}else{
								snatch.getAchimentMap().put(achieveId, fightNum);
							}
						}
						break;
					case TYPE2:
						if(fightData.result.isWin()){
							if(achieveConfig.fightNum <= fightNum){//成就已完成,发放奖励,设置下一个成就
								SnatchAchieveConfig nextAchieve = SnatchService.getNextAchieve(achieveId);
								snatch.getAchimentMap().remove(achieveId);
								if(nextAchieve != null){//还有下一个成就
									snatch.getAchimentMap().put(nextAchieve.achieveId, 0);
								}
								int level = ActorHelper.getActorLevel(snatch.getPkId());
								rewardList.addAll(achieveConfig.getRewardList(level));
								RewardObject extReward = achieveConfig.getExeReward(actorLevel);
								if(extReward != null){
									rewardList.add(extReward);
								}
							}else{
								snatch.getAchimentMap().put(achieveId, fightNum);
							}
						}
						break;
					default:
						break;
					}
				}
			}
			snatchDao.update(snatch);
			SnatchPushHelper.pushAchimentProgress(actorId, snatch.getAchimentMap());
			SnatchPushHelper.pushActorScore(actorId, snatch.score);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		
		if(rewardList.isEmpty() == false){
			sendReward(actorId, rewardList);
		}
		
		// 处理本次抢夺的奖励
		goldSnatchResultImpl.rewardSnatch(actorId, targetActorId, enemyType, winlevel, snatchVo, config);
		
		long golds = 0;
		if(enemyType == SnatchEnemyType.ACTOR){
			golds = actorFacade.getActor(targetActorId).gold;
		}else{
			golds = snatchRobotFacade.getSnatchEnemy(targetActorId).gold;
		}
		snatchVo.golds = golds;
		// 最先下发战斗结果(注意下面的顺序，得先发下战斗结果，再下发其他的东东)
		SnatchPushHelper.pushBattleResult(actorId, fightData, snatchVo, rewardList,snatch.snatchNum);

		// 发送消息告诉对方		
		notifyFacade.createSnatch(actorId, actorLevel, enemyType, targetActorId, targetActorName, targetLevel, winlevel.isWin(),
				snatchVo.score, snatchVo.goodsId, snatchVo.goodsNum, snatchVo.goodsId, snatchVo.goodsNum, fightData.getBytes());
		
		
		// 告诉通知我的人，已经帮忙抢夺了目标对手
		if (notifyId > 0) {
			notifyFacade.createSnatchRevenge(actorId, notifyId, snatchVo.goodsId, snatchVo.goodsNum, winlevel);
			// 只允许反击一次.删除 notifyId通知
			notifyFacade.remove(actorId, notifyId);
		}

		// 抛出战斗结果事件
		eventBus.post(new SnatchResultEvent(actorId, enemyType, targetActorId, winlevel, snatchVo.score, snatchVo.goodsId, snatchVo.goodsNum));

		// oss日志
		Actor actor = actorFacade.getActor(actorId);
		GameOssLogger.snatchResult(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, targetActorId,winlevel,
				snatchVo.score, snatchVo.goodsId, snatchVo.goodsNum);

	}

	private void sendReward(long actorId, List<RewardObject> rewardList) {
		for(RewardObject reward:rewardList){
			sendReward(actorId,reward.id,reward.num,reward.rewardType.getCode());
		}
	}

	@Override
	public Result exchange(long actorId, int type, int cfgId,int num) {
		if(type == ExchangeType.SCORE_EXCHANGE){
			Snatch snatch = snatchDao.get(actorId);
			ChainLock lock = LockUtils.getLock(snatch);
			try{
				lock.lock();
				SnatchExchangeConfig config = SnatchExchangeService.getReward(cfgId);
				int needScore = config.needScore * num;
				if(snatch.score < needScore){
					return Result.valueOf(EXCHANGE_FAIL_SCORE_NOT_ENOUGH);
				}
				snatch.score -= needScore;
				snatchDao.update(snatch);
				sendReward(actorId, config.rewardId, config.rewardNum * num, config.rewardType);
				SnatchPushHelper.pushActorScore(actorId, snatch.score);
				return Result.valueOf();
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
		return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
	}
	
	@Override
	public void onLogin(long actorId) {
		Snatch snatch = snatchDao.get(actorId);
		ChainLock lock = LockUtils.getLock(snatch);
		try{
			lock.lock();
			if(DateUtils.isToday(snatch.lastFightTime) == false){
				snatch.snatchNum = SnatchService.get().snatchMaxNum;
				snatch.useSnatch = 0;
				snatch.setAchieveMent(SnatchHelper.getInitAchieve());
				snatchDao.update(snatch);
			}
			if(DateUtils.isToday(snatch.lastTicketTime) == false){
				snatch.ticketNum = 0;
				snatch.lastTicketTime = TimeUtils.getNow();
				snatchDao.update(snatch);
			}
			if(DateUtils.isToday(snatch.getTime) == false){
				snatch.getTime = TimeUtils.getNow();
				snatchDao.update(snatch);
			}
			flushFightNum(actorId);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * 发放奖励
	 * @param actorId
	 * @param id
	 * @param num
	 * @param rewardType
	 */
	private void sendReward(long actorId, int id, int num, int type) {
		RewardType rewardType = RewardType.getType(type);
		switch(rewardType){
		case GOODS:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.SNATCH_EXCHANGE, id, num);
			break;
		case HEROSOUL:
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.SNATCH_EXCHANGE, id, num);
			break;
		default:
			LOGGER.error(String.format("类型错误，type:[%s]", rewardType.getCode()));
			break;
		}
	}
	
	@Override
	public void onZero() {
		for(long actorId:playerSession.onlineActorList()){
			Snatch snatch = snatchDao.get(actorId);
			ChainLock lock = LockUtils.getLock(snatch);
			try{
				lock.lock();
				snatch.setAchieveMent(SnatchHelper.getInitAchieve());
				snatch.lastFightTime = TimeUtils.getNow();
				snatch.lastTicketTime = TimeUtils.getNow();
				snatch.ticketNum = 0;
				snatch.snatchNum = SnatchService.get().snatchMaxNum;
				snatch.useSnatch = 0;
				snatch.getTime = 0;
				snatchDao.update(snatch);
				SnatchConfig config = SnatchService.get();
				int nextCostTicket = FormulaHelper.executeCeilInt(config.costTicket, snatch.ticketNum);
				SnatchPushHelper.pushBuyInfo(actorId, nextCostTicket, snatch.ticketNum,snatch.snatchNum);
				SnatchPushHelper.pushAchimentProgress(actorId, snatch.getAchimentMap());
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}

	@Override
	public TResult<SnatchNumResponse> buySnatchNum(long actorId) {
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		if(vipPrivilege == null || vipPrivilege.energyNum < 1){
			return TResult.valueOf(GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH);
		}
		Snatch snatch = get(actorId);
		if(snatch.ticketNum >= vipPrivilege.energyNum){
			return TResult.valueOf(GameStatusCodeConstant.SNATCH_NUM_NOT_ENOUGH);
		}
		if(snatch.snatchNum > 0){
			return TResult.valueOf(GameStatusCodeConstant.SNATCH_NUM_NOT_USED);
		}
		SnatchConfig config = SnatchService.get();
		int ticketNum = FormulaHelper.executeCeilInt(config.costTicket, snatch.snatchNum);
		boolean flag = vipFacade.decreaseTicket(actorId, TicketDecreaseType.SNATCH_NUM, ticketNum, 0, 0);
		if(flag == false){
			return TResult.valueOf(StatusCode.TICKET_NOT_ENOUGH);
		}
		ChainLock lock = LockUtils.getLock(snatch);
		try{
			lock.lock();
			snatch.snatchNum = config.snatchMaxNum;
			snatch.ticketNum ++;
			snatch.lastTicketTime = TimeUtils.getNow();
			snatchDao.update(snatch);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		int nextCostTicket = FormulaHelper.executeCeilInt(config.costTicket, snatch.ticketNum);
		SnatchNumResponse response = new SnatchNumResponse(nextCostTicket,snatch.ticketNum,snatch.snatchNum);
		return TResult.sucess(response);
	}

	@Override
	public Result fullSnatchNum(long actorId) {
		Snatch snatch = get(actorId);
		if(snatch.snatchNum >0){
			return Result.valueOf(GameStatusCodeConstant.SNATCH_GET_NUM_FAIL);
		}
		if(DateUtils.isToday(snatch.getTime) == false){
			return Result.valueOf(GameStatusCodeConstant.SNATCH_GET);
		}
		SnatchConfig config = SnatchService.get();
		ChainLock lock = LockUtils.getLock(snatch);
		try{
			lock.lock();
			snatch.snatchNum = config.snatchMaxNum;
			snatch.getTime = TimeUtils.getNow();
			snatchDao.update(snatch);
			int nextCostTicket = FormulaHelper.executeCeilInt(config.costTicket, snatch.ticketNum);
			SnatchPushHelper.pushBuyInfo(actorId, nextCostTicket, snatch.ticketNum,snatch.snatchNum);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(new Runnable() {//恢复战斗次数调度
			@Override
			public void run() {
				Set<Long> actors = playerSession.onlineActorList();
				for (Long actorId : actors) {
					flushFightNum(actorId);
				}
			}
		}, 1);
	}
	
	private void flushFightNum(Long actorId) {
		Snatch snatch = snatchDao.get(actorId);
		SnatchConfig config = SnatchService.get();
		ChainLock lock = LockUtils.getLock(snatch);
		try {
			lock.lock();
			if(snatch.isAddUseNum(config.flushTime, config.snatchMaxNum)){
				snatchDao.update(snatch);
				SnatchPushHelper.pushFightNum(actorId, snatch);
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
	}

}
