package com.jtang.gameserver.module.power.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.CROSS_BATTLE_START;
import static com.jiatang.common.GameStatusCodeConstant.DEMON_JOIN_IN;
import static com.jiatang.common.GameStatusCodeConstant.POWER_ALREADY_DEGRADATION_OR_OUT;
import static com.jiatang.common.GameStatusCodeConstant.POWER_UN_ABLE_CHALLENGE_SELF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.event.EventBus;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.PowerBattleEvent;
import com.jtang.gameserver.component.event.PowerRankChangeEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.dataconfig.model.NoticeConfig;
import com.jtang.gameserver.dataconfig.model.PowerBattleConfig;
import com.jtang.gameserver.dataconfig.model.PowerGlobalConfig;
import com.jtang.gameserver.dataconfig.model.PowerRewardConfig;
import com.jtang.gameserver.dataconfig.model.PowerShopConfig;
import com.jtang.gameserver.dataconfig.service.GmService;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dataconfig.service.NoticeService;
import com.jtang.gameserver.dataconfig.service.PowerRewardService;
import com.jtang.gameserver.dataconfig.service.PowerService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Power;
import com.jtang.gameserver.dbproxy.entity.PowerExt;
import com.jtang.gameserver.module.battle.constant.BattleSkipPlayType;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackPlayerRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.chat.facade.ChatFacade;
import com.jtang.gameserver.module.crossbattle.facade.CrossBattleCallbackFacade;
import com.jtang.gameserver.module.demon.facade.DemonFacade;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.notice.facade.NoticeFacade;
import com.jtang.gameserver.module.notice.type.NoticeType;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.power.constant.PowerRule;
import com.jtang.gameserver.module.power.dao.PowerDao;
import com.jtang.gameserver.module.power.dao.PowerExtDao;
import com.jtang.gameserver.module.power.facade.PowerFacade;
import com.jtang.gameserver.module.power.handler.response.PowerFightResponse;
import com.jtang.gameserver.module.power.handler.response.PowerFlushResponse;
import com.jtang.gameserver.module.power.handler.response.PowerInfoResponse;
import com.jtang.gameserver.module.power.handler.response.PowerShopInfoResponse;
import com.jtang.gameserver.module.power.handler.response.RankListResponse;
import com.jtang.gameserver.module.power.helper.PowerPushHelper;
import com.jtang.gameserver.module.power.model.PowerShopVO;
import com.jtang.gameserver.module.power.model.PowerVO;
import com.jtang.gameserver.module.power.type.FightType;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.sysmail.type.SysmailType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class PowerFacadeImpl implements PowerFacade, ActorLoginListener, ApplicationListener<ContextRefreshedEvent>, BattleCallBack,ZeroListener {
	
	@Autowired
	private PowerDao powerDao;
	@Autowired
	private BattleFacade battleFacade;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private NotifyFacade notifyFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private Schedule schedule;
	@Autowired
	private NoticeFacade noticeFacade;
	@Autowired
	private DemonFacade demoFacade; 
	@Autowired
	private EventBus eventBus;
	@Autowired
	private SysmailFacade sysmailFacade;
	@Autowired
	private ChatFacade chatFacade;
	@Autowired
	private PowerExtDao powerExtDao;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private IconFacade iconFacade;
	
	@Autowired
	private CrossBattleCallbackFacade crossBattleCallbackFacade;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PowerFacadeImpl.class);
	
	/**
	 * 交换排名锁
	 */
	private static Object CHANGE_RANK_LOCK = new Object();

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) { 

		// 每晚x点定时触发
		schedule.addFixedTime(rankAward(), PowerRule.POWER_AWARD_TIME);

		// 开服生成，今天x点前3名奖励的魂魄
		PowerRule.randRewardSoulId();
	}

	/**
	 * 每天定点给最强势力排行榜上的角色发放奖励
	 * 
	 * @return
	 */
	public Runnable rankAward() {
		return new Runnable() {
			@Override
			public void run() {
				
				Collection<Power> powers = powerDao.getTopRanks(PowerRule.MIN_RANK,PowerRule.MAX_RANK);
				Map<Long, Integer> rewardActorMaps = new HashMap<>();
				
				//有并发问题。加个锁
				synchronized (powers) {
					for (Power p : powers) {
						rewardActorMaps.put(p.getActorId(), p.getRank());
					}
				}
				
				//奖励物品
				for (Entry<Long, Integer> powerEntry : rewardActorMaps.entrySet()) {
					long actorId = powerEntry.getKey();
					int rank = powerEntry.getValue();

					PowerRewardConfig config = PowerRewardService.get(rank);
					if (config == null) {
						continue;
					}
					
					// 发送奖励
					sysmailFacade.sendSysmail(actorId, SysmailType.POWER, config.rewardList,rank);
					
				}
				//记录发奖oss
				GameOssLogger.powerRewardActor(rewardActorMaps);
			}
		};
	}
	
	@Override
	public List<Long> getRankList(int maxRank) {
		List<Long> rankList = new ArrayList<>();
		Long actorId = 0L;
		for (int i = 1; i <= maxRank; i++) {
			actorId = powerDao.getActorId(i);
			if (actorId != null && actorId > 0) {
				rankList.add(actorId);
			}
		}

		return rankList;
	}
	
	private PowerVO generatePowerVO(Power power) {
		Actor actor = actorFacade.getActor(power.getActorId());
		IconVO iconvo = iconFacade.getIconVO(power.getActorId());
		if (actor != null && power != null && iconvo != null){
			LineupFightModel lineup = LineupHelper.getLineupFight(power.actorId);
			float atk = 0f;
			float def = 0f;
			float hp = 0f;
			for(Entry<Integer,HeroVO> entry:lineup.getHeros().entrySet()){
				HeroVO heroVO = entry.getValue();
				Map<AttackerAttributeKey, Integer> map = LineupHelper.getInstance().getEquipAndBaseAtt(power.actorId, heroVO.heroId);
				atk += map.get(AttackerAttributeKey.ATK);
				def += map.get(AttackerAttributeKey.DEFENSE);
				hp += map.get(AttackerAttributeKey.HP);
			}
			int powerNum = (int) Math.ceil(atk/10 + def/20 + hp/10);
			PowerVO powerVO = new PowerVO(power.getRank(), power.getActorId(), actor.level, actor.actorName, vipFacade.getVipLevel(power.actorId), iconvo,powerNum);
			return powerVO;
		} else {
			return null;
		}
	}

	@Override
	public Result challenge(long actorId, long targetActorId) {
		if (actorId == targetActorId) {
			return Result.valueOf(POWER_UN_ABLE_CHALLENGE_SELF);
		}
		
		//判断对方是否在参加集众降魔
		if (demoFacade.isDemon(actorId) || demoFacade.isDemon(targetActorId)) {
			return Result.valueOf(DEMON_JOIN_IN);
		}
		//判断是否参与了跨服战
		if (crossBattleCallbackFacade.isInCrossBattle(actorId) || crossBattleCallbackFacade.isInCrossBattle(targetActorId)) {
			return Result.valueOf(CROSS_BATTLE_START);
		}
		
		Power power = powerDao.getPower(actorId);
		Power targetPower = powerDao.getPower(targetActorId);
		Actor actor = actorFacade.getActor(actorId);
		Actor targetActor = actorFacade.getActor(targetActorId);
		if (targetPower == null) {
			return Result.valueOf(POWER_ALREADY_DEGRADATION_OR_OUT);
		}
//		if(power.getRank() < targetPower.getRank()) {
//			return Result.valueOf(POWER_CHALLENGE_FORBID_LOW_RANK);
//		}
//		if (actor.energy < PowerRule.POWER_FIGHT_COST_ENERGY) {
//			return Result.valueOf(POWER_CHALLENGE_ENERGY_NOT_ENOUGH);
//		}
		PowerExt powerExt = powerExtDao.getPowerExt(actorId);
		PowerGlobalConfig globalConfig = PowerService.getGlobalConfig();
		ChainLock lock = LockUtils.getLock(powerExt);
		try{
			lock.lock();
			int now = TimeUtils.getNow();
			if(now < powerExt.fightTime + globalConfig.fightTime){
				return Result.valueOf(GameStatusCodeConstant.POWER_TIME_NOT_OVER);
			}
			powerExt.fightTime = TimeUtils.getNow();
			powerExtDao.update(powerExt);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
//		if (targetActor.level - actor.level >= PowerRule.POWER_CHALLENGE_LEVEL_DIFFER) {
//			return Result.valueOf(POWER_CHALLENGE_LEVEL_NOT_ENOUGH);
//		}

		LineupFightModel atkTeam = LineupHelper.getLineupFight(actorId);
		LineupFightModel defTeam = LineupHelper.getLineupFight(targetActorId);
		MapConfig map = MapService.get(PowerRule.POWER_RANK_BATTLE_1V1_MAP_ID);


		AttackPlayerRequest attackReq = new AttackPlayerRequest(EventKey.POWER_BATTLE, map, actorId, atkTeam, targetActorId, defTeam, actor.morale,
				targetActor.morale, power.getRank(), BattleType.POWER);

		Result result = battleFacade.submitAtkPlayerRequest(attackReq, this);
		// 战斗没有开打,前期检查没有通过.
		if (result.isFail()) {
			return Result.valueOf(result.statusCode);
		}

		return result;
	}

	@Override
	public void execute(BattleResult result) {
		fightFinish(result);
	}

	/**
	 * 战斗完成处理方法
	 * 
	 * @param battleResult
	 */
	private void fightFinish(BattleResult battleResult) {
		AttackPlayerRequest attackReq = (AttackPlayerRequest) battleResult.battleReq;
		PowerFightResponse response = new PowerFightResponse();
		
		FightData data = battleResult.fightData;
		response.data = data;
		data.setCanSkipPlay(BattleSkipPlayType.PVP_CLIENT_DECIDE);
		WinLevel result = data.result;
		PowerGlobalConfig globalConfig = PowerService.getGlobalConfig();
		
		boolean captureSuccess = false;

		Power power = powerDao.getPower(attackReq.actorId);
		Power targetPower = powerDao.getPower(attackReq.targetActorId);
		synchronized(CHANGE_RANK_LOCK){
			int oldRank = power.getRank();
			int targetOldRank = targetPower.getRank();
			PowerExt powerExt = powerExtDao.getPowerExt(power.actorId);
			PowerBattleConfig config = PowerService.getBattleConfig(oldRank);
			if (result.isWin()) {
				// 当前旧排名
				if(oldRank < targetOldRank){//挑战比自己低的排名
					int morale = actorFacade.costMorale(targetPower.actorId, config.costMorale);
					response.fightType = FightType.DOWN.getCode();
					response.costMorale = morale;
				}else{
					int morale = actorFacade.costMorale(targetPower.actorId, config.costMorale);
					//挑战比自己高的排名成功交换排名
					powerDao.changeRank(power, targetPower);
					response.fightType = FightType.UP.getCode();
					response.costMorale = morale;
					//向上挑战赢了才发系统公告
					NoticeConfig noticeConfig = NoticeService.get(NoticeType.POWER_RANK.getCode());
					boolean powerNotNull = (power != null && power.rank <= noticeConfig.getCondition());
					boolean targetPowerNotNull = (targetPower != null && targetPower.rank <= noticeConfig.getCondition());
					Actor actor = actorFacade.getActor(attackReq.actorId);
					Actor targetActor = actorFacade.getActor(attackReq.targetActorId);
					if (powerNotNull || targetPowerNotNull) {
						int vipLevel = vipFacade.getVipLevel(actor.getPkId());
						int targetVipLevel = vipFacade.getVipLevel(targetActor.getPkId());
						int isWin = battleResult.fightData.result.isWin() ? 1 : 2;
						int isFirst = power.rank == 1 && isWin == 1 ? 1:0;
						chatFacade.sendPowerChat(actor.actorName, actor.getPkId(),
								actor.level, vipLevel, isWin, targetActor.level,
								targetVipLevel, targetActor.actorName, isFirst);
					}
				}
				if(powerExt.historyRank > power.rank){//历史排名更新
					int oldHistoryRank = powerExt.historyRank;
					powerExt.historyRank = power.rank;
					List<RewardObject> list = PowerService.getRankReward(powerExt.historyRank, oldHistoryRank);
					if(list.size() > 0){
						sendReward(power.actorId,list);
						response.list = list;
					}
					//挑战成功抛出排名变更事件
					int actorLevel = ActorHelper.getActorLevel(attackReq.actorId);
					eventBus.post(new PowerRankChangeEvent(attackReq.actorId, actorLevel, oldRank, power.getRank()));
				}
				int moraleNum = powerExt.moraleNum + config.addMorale;
				int addMoral = 0;
				if(moraleNum <= globalConfig.dayMorale){
					addMoral = config.addMorale;
				} else {
					addMoral = globalConfig.dayMorale - moraleNum;
				}
				addMoral = addMoral > 0? addMoral : 0;
				powerExt.moraleNum += addMoral;
				actorFacade.addMorale(power.actorId, addMoral);
				response.addMorale = addMoral;
				powerExtDao.update(powerExt);
				response.fightTime = powerExt.fightTime;
				captureSuccess = true;
	
			}else{
				// 当前旧排名
				if(oldRank < targetOldRank){//挑战比自己低的排名
					response.fightType = FightType.DOWN.getCode();
				}else{
					response.fightType = FightType.UP.getCode();
				}
				powerExtDao.update(powerExt);
				response.fightTime = powerExt.fightTime;
			}
			if(powerExt.historyRank < power.rank && powerExt.historyRank == PowerService.getMaxRewardRank()){
				response.historyRank = power.rank;
			}else{
				response.historyRank = powerExt.historyRank;
			}
		}
		response.targetRank = power.getRank();
		response.targetId = targetPower.actorId;
		response.captureSuccess = captureSuccess ? 1 : 0;
		PowerPushHelper.pushFightResult(attackReq.actorId,response);
		
		/**
		 * 无论挑战结果如何，挑战双方都要在消息系统收到消息；消息内容： “[玩家名]在最强势力排行中挑战你成功，夺得你的名次，现在你是第几名”
		 * “[玩家名]在最强势力排行中挑战你成功，夺得你的名次，现在你在50名之外”
		 */
		notifyFacade.createPowerFight(result.isWin(), captureSuccess, attackReq.actorId, power.getRank(), attackReq.targetActorId,
				targetPower.getRank(), data.getBytes());
		
		
		eventBus.post(new PowerBattleEvent(attackReq.actorId));
	}
	
	@Override
	public Power getPower(long actorId) {
		return powerDao.getPower(actorId);
	}

	@Override
	public TResult<PowerInfoResponse> getInfo(long actorId) {
		PowerGlobalConfig globalConfig = PowerService.getGlobalConfig();
		int level = ActorHelper.getActorLevel(actorId);
		if(globalConfig.level > level){
			return TResult.valueOf(GameStatusCodeConstant.POWER_LEVEL_NOT_ENOUGH);
		}
		Power power = powerDao.getPower(actorId);
		PowerExt powerExt = powerExtDao.getPowerExt(actorId);
		PowerInfoResponse response = new PowerInfoResponse(powerExt,power.rank);
		return TResult.sucess(response);
	}

	@Override
	public TResult<PowerFlushResponse> buyFightNum(long actorId) {
		PowerGlobalConfig globalConfig = PowerService.getGlobalConfig();
		int level = ActorHelper.getActorLevel(actorId);
		if(globalConfig.level > level){
			return TResult.valueOf(GameStatusCodeConstant.POWER_LEVEL_NOT_ENOUGH);
		}
		PowerExt powerExt = powerExtDao.getPowerExt(actorId);
		ChainLock lock = LockUtils.getLock(powerExt);
		try{
			lock.lock();
			if(powerExt.fightTime <= TimeUtils.getNow() - globalConfig.fightTime){
				return TResult.valueOf(GameStatusCodeConstant.POWER_FIGHT_NOT_USE);
			}
			int ticketNum = PowerService.getFlushCostTicket(powerExt.flushNum + 1);
			if(vipFacade.decreaseTicket(actorId, TicketDecreaseType.POWER_BUY, ticketNum, 0, 0) == false){
				return TResult.valueOf(StatusCode.TICKET_NOT_ENOUGH);
			}
			powerExt.flushNum += 1;
			powerExt.flushTime = TimeUtils.getNow();
			powerExt.fightTime = 0;
			powerExtDao.update(powerExt);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		PowerFlushResponse response = new PowerFlushResponse(powerExt.flushNum);
		return TResult.sucess(response);
	}
	

	@Override
	public Result exchange(long actorId, int exchangeId,int exchangeNum) {
		PowerShopConfig config = PowerService.getShopConfig(exchangeId);
		if(config == null){
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		PowerExt powerExt = powerExtDao.getPowerExt(actorId);
		ChainLock lock = LockUtils.getLock(powerExt);
		try{
			lock.lock();
			PowerGlobalConfig globalConfig = PowerService.getGlobalConfig();
			PowerShopVO shopVO = powerExt.rewardMap.get(exchangeId);
			if(shopVO.num < exchangeNum){
				return Result.valueOf(GameStatusCodeConstant.POWER_SHOP_NOT_ENOUGH);
			}
			int goodsNum = goodsFacade.getCount(actorId, globalConfig.goodsId);
			int needPowerNum = config.costGoods * exchangeNum;
			if(needPowerNum > goodsNum){
				return Result.valueOf(GameStatusCodeConstant.POWER_NUM_NOT_ENOUGH);
			}
			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.POWER_SHOP, globalConfig.goodsId, needPowerNum);
			RewardType type = RewardType.getType(config.type);
			int id = config.itemId;
			int rewardNum = config.num;
			shopVO.num -= exchangeNum;
			RewardObject rewardObject = new RewardObject(type,id,rewardNum);
			sendReward(actorId, rewardObject);
			powerExtDao.update(powerExt);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}
	
	@Override
	public void onLogin(long actorId) {
		int level = ActorHelper.getActorLevel(actorId);
		PowerGlobalConfig globalConfig = PowerService.getGlobalConfig();
		if(GmService.isGm(actorId) == false && globalConfig.level <= level){
			Actor actor = actorFacade.getActor(actorId);
			powerDao.add(actorId, actor.serverId);
			PowerExt powerExt = powerExtDao.getPowerExt(actorId);
			if(powerExt.fightTime != 0 && DateUtils.isToday(powerExt.fightTime) == false){
				ChainLock lock = LockUtils.getLock(powerExt);
				try{
					lock.lock();
					powerExt.reset();
					powerExtDao.update(powerExt);
				}catch(Exception e){
					LOGGER.error("{}",e);
				}finally{
					lock.unlock();
				}
			}
		}
	}
	
	@Override
	public void onZero() {
		for(Long actorId:playerSession.onlineActorList()){
			PowerExt powerExt = powerExtDao.getPowerExt(actorId);
			ChainLock lock = LockUtils.getLock(powerExt);
			try{
				lock.lock();
				powerExt.reset();
				powerExtDao.update(powerExt);
				PowerPushHelper.pushPowerReset(actorId);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}
	
	private void sendReward(long actorId, List<RewardObject> rewardList) {
		for(RewardObject rewardObject:rewardList){
			sendReward(actorId, rewardObject);
		}
	}

	private void sendReward(long actorId, RewardObject rewardObject) {
		switch (rewardObject.rewardType) {
		case EQUIP: {
			equipFacade.addEquip(actorId, EquipAddType.POWER, rewardObject.id);
			break;
		}
		case GOLD: {
			actorFacade.addGold(actorId, GoldAddType.POWER, rewardObject.num);
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.POWER, rewardObject.id, rewardObject.num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.POWER, rewardObject.id, rewardObject.num);
			break;
		}
		case TICKET:{
			vipFacade.addTicket(actorId, TicketAddType.POWER, rewardObject.num);
		}
		default:
		}
	}

	@Override
	public TResult<RankListResponse> getTopRank() {
		int viewRank = PowerService.getGlobalConfig().viewRank;
		Collection<Power> powerList = powerDao.getTopRanks(1, viewRank);
		List<PowerVO> powerVOList = new ArrayList<>();
		for(Power p : powerList){
			powerVOList.add(generatePowerVO(p));
		}
		RankListResponse response = new RankListResponse(powerVOList);
		return TResult.sucess(response);
	}

	@Override
	public TResult<RankListResponse> getFightRank(long actorId) {
		PowerGlobalConfig globalConfig = PowerService.getGlobalConfig();
		int level = ActorHelper.getActorLevel(actorId);
		if(globalConfig.level > level){
			return TResult.valueOf(GameStatusCodeConstant.POWER_LEVEL_NOT_ENOUGH);
		}
		Power power = powerDao.getPower(actorId);
		//Set<PowerVO> list = new HashSet<>();
		Collection<Power> powerList = powerDao.getPowerList(globalConfig.viewRank, power.rank, globalConfig.viewUp, globalConfig.viewDown, globalConfig.getUpList(power.rank));
		List<Power> fightList = new ArrayList<>();
		List<Power> upList = new ArrayList<>();
		List<Power> downList = new ArrayList<>();
		List<PowerVO> list = new ArrayList<>();
		for (Power p : powerList) {
			if(p.actorId == power.actorId){
				continue;
			}else if (globalConfig.getUpList(power.rank).contains(p.rank)) {
				fightList.add(p);
			} else if (p.rank < power.rank && p.rank >= Math.max(1,power.rank - globalConfig.viewUp)) {
				upList.add(p);
			} else {
				downList.add(p);
			}
		}
		int size = globalConfig.upListNum + globalConfig.upRankNum + globalConfig.downRankNum;
		if(fightList.isEmpty() == false){//向上节点人数
			int[] randomPower = RandomUtils.uniqueRandom(size - globalConfig.upRankNum - globalConfig.downRankNum, 0, fightList.size() - 1);
			for (int i = 0; i < randomPower.length; i++) {
				Power p = fightList.get(randomPower[i]);
				list.add(generatePowerVO(p));
				size --;
			}
		}
		if(upList.isEmpty() == false){//向上周围人数
			int[] randomPower = RandomUtils.uniqueRandom(size - globalConfig.downRankNum, 0, upList.size() -1);
			for(int i = 0; i < randomPower.length; i++){
				Power p = upList.get(randomPower[i]);
				list.add(generatePowerVO(p));
				size --;
			}
		}
		if(downList.isEmpty() == false){//向下周围人数
			int[] randomPower = RandomUtils.uniqueRandom(size, 0, downList.size() -1);
			for(int i = 0; i < randomPower.length; i++){
				Power p = downList.get(randomPower[i]);
				list.add(generatePowerVO(p));
			}
		}
		RankListResponse response = new RankListResponse(new ArrayList<>(list));
		return TResult.sucess(response);
	}

	@Override
	public TResult<PowerShopInfoResponse> getShopInfo(long actorId) {
		PowerExt powerExt = powerExtDao.getPowerExt(actorId);
		PowerGlobalConfig globalConfig = PowerService.getGlobalConfig();
		int level = ActorHelper.getActorLevel(actorId);
		ChainLock lock = LockUtils.getLock(powerExt);
		if(powerExt.rewardMap.isEmpty()){
			try{
				lock.lock();
				powerExt.rewardMap = PowerService.initShop();
				powerExtDao.update(powerExt);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
		if(globalConfig.level > level){
			return TResult.valueOf(GameStatusCodeConstant.POWER_LEVEL_NOT_ENOUGH);
		}
		PowerShopInfoResponse response = new PowerShopInfoResponse(powerExt.flushShopNum, powerExt.rewardMap);
		return TResult.sucess(response);
	}

	@Override
	public TResult<PowerShopInfoResponse> shopFlush(long actorId) {
		PowerGlobalConfig globalConfig = PowerService.getGlobalConfig();
		int level = ActorHelper.getActorLevel(actorId);
		if(globalConfig.level > level){
			return TResult.valueOf(GameStatusCodeConstant.POWER_LEVEL_NOT_ENOUGH);
		}
		PowerExt powerExt = powerExtDao.getPowerExt(actorId);
		ChainLock lock = LockUtils.getLock(powerExt);
		try{
			lock.lock();
			int costGoods = PowerService.getShopFlushCostGoods(powerExt.flushShopNum + 1);
			int goodsNum = goodsFacade.getCount(actorId, globalConfig.goodsId);
			if(goodsNum < costGoods){
				return TResult.valueOf(GameStatusCodeConstant.POWER_NOT_ENOUGH);
			}
			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.POWER_SHOP_FLUSH, globalConfig.goodsId,costGoods);
			powerExt.flushShopNum ++;
			powerExt.flushShopTime = TimeUtils.getNow();
			powerExt.rewardMap = PowerService.initShop();
			powerExtDao.update(powerExt);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		
		return getShopInfo(actorId);
	}

}
