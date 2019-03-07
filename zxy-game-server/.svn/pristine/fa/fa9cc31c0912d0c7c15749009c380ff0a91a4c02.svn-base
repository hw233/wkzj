package com.jtang.gameserver.module.extapp.beast.facade.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.admin.facade.MaintainFacade;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.dataconfig.model.ActorUpgradeConfig;
import com.jtang.gameserver.dataconfig.model.BeastMonsterConfig;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.dataconfig.service.ActorService;
import com.jtang.gameserver.dataconfig.service.BeastService;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Beast;
import com.jtang.gameserver.dbproxy.entity.BeastGlobal;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackMonsterRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.extapp.beast.dao.BeastDao;
import com.jtang.gameserver.module.extapp.beast.dao.BeastGlobalDao;
import com.jtang.gameserver.module.extapp.beast.facade.BeastFacade;
import com.jtang.gameserver.module.extapp.beast.handler.response.BeastAttackResponse;
import com.jtang.gameserver.module.extapp.beast.handler.response.BeastInfoResponse;
import com.jtang.gameserver.module.extapp.beast.handler.response.BeastStatusResponse;
import com.jtang.gameserver.module.extapp.beast.helper.BeastPushHelper;
import com.jtang.gameserver.module.extapp.beast.type.BeastEndType;
import com.jtang.gameserver.module.extapp.beast.type.BeastSendMsgType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.user.dao.ActorDao;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.ReputationAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class BeastFacadeImpl implements BeastFacade, BattleCallBack, ApplicationListener<ContextRefreshedEvent>,ZeroListener,ActorLoginListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(BeastFacadeImpl.class);
	
	@Autowired
	BeastDao beastDao;
	
	@Autowired
	ActorDao actorDao;
	
	@Autowired
	BeastGlobalDao beastGlobalDao;
	
	@Autowired
	VipFacade vipFacade;
	
	@Autowired
	ActorFacade actorFacade;
	
	@Autowired
	GoodsFacade goodsFacade;
	
	@Autowired
	EquipFacade equipFacade;
	
	@Autowired
	HeroSoulFacade heroSoulFacade;
	
	@Autowired
	BattleFacade battleFacade;
	
	@Autowired
	MaintainFacade maintainFacade;
	
	@Autowired
	PlayerSession playerSession;
	
	@Autowired
	Schedule schedule;
	
	
	public volatile Map<Integer, MonsterVO> monsterVO =  new ConcurrentHashMap<Integer, MonsterVO>();
	
	public Collection<Long> beastActorList = Collections.synchronizedList(new ArrayList<Long>()); 

	private volatile boolean isActOpen = false;
	
	private static BeastSendMsgType sendFlag = BeastSendMsgType.NONE;
	
	private void checkStartNotice() {
		int startTime = BeastService.getActivityStartTime();
		int endTime = BeastService.BEAST_GLOBAL_CONFIG.delayTime + startTime;
		int now = TimeUtils.getNow();
		String msg = null;
		if (startTime >= now && startTime - now < 62 && endTime >= now && isActOpen == false && (sendFlag == BeastSendMsgType.NONE || sendFlag == BeastSendMsgType.ESCAPE_END  || sendFlag == BeastSendMsgType.BEAT_END)) {
			msg = String.format(BeastService.BEAST_GLOBAL_CONFIG.beforeStart);
			sendFlag = BeastSendMsgType.BEFORE_START;
		}
		if (now >= startTime && Math.abs(startTime - now) < 2 && isActOpen == false && sendFlag == BeastSendMsgType.BEFORE_START) {
			msg = String.format(BeastService.BEAST_GLOBAL_CONFIG.start);
			sendFlag = BeastSendMsgType.START;
		}
		if (now >= endTime && isActOpen == true && sendFlag == BeastSendMsgType.START) {
			msg = String.format(BeastService.BEAST_GLOBAL_CONFIG.escapeEnd);
			sendFlag = BeastSendMsgType.ESCAPE_END;
		}
		if(msg != null) {
			maintainFacade.sendNotice(msg, 1, 0, new ArrayList<Integer>());
		}
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(new Runnable() {

			@Override
			public void run() {
				if (BeastService.isOpen() == true) {
					checkStartNotice();
				}
				
				if (isActOpen == false && sendFlag == BeastSendMsgType.START) { // 未开放时才可开放
					if (monsterVO.isEmpty() == true) {
						start();
						isActOpen = true;
					}
				}
				
				if (isActOpen == true) {// 开放时才可关闭
					if (monsterVO.isEmpty() == false && TimeUtils.getNow() > BeastService.getActivityEndTime()) {
						end(BeastEndType.ESCAPE);
						sendFlag = BeastSendMsgType.NONE;
						isActOpen = false;
					}
				}
			}
		}, 2);
	}
	
	
	private void start() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("年兽开始");
		}
		beastActorList.clear();
		createBoss(BeastService.BEAST_GLOBAL_CONFIG.monsterConfigId);
		BeastPushHelper.pushAllBeastAttacker(playerSession.onlineActorList(), (byte)1, (byte)100);
	}
	
	private void end(BeastEndType type) {
		int startTime = BeastService.getActivityStartTime();
		int endTime = BeastService.BEAST_GLOBAL_CONFIG.delayTime + startTime;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("年兽结束, 配置结束时间为 :" + DateUtils.formatTime(new Date(endTime * 1000L)));
		}
		byte percent = 0;	
		BeastGlobal global = beastGlobalDao.get(BeastService.BEAST_GLOBAL_CONFIG.monsterConfigId);
		if (type == BeastEndType.BEAT) {
			isActOpen = false;
			global.bossDeadUseTime = TimeUtils.getNow() - startTime;
			sendFlag = BeastSendMsgType.BEAT_END;
		} else {
			TResult<Byte> result = this.getBloodPrecent();
			if (result.isOk()) {
				percent = result.item.byteValue();
			}
			global.bossDeadUseTime = BeastService.BEAST_GLOBAL_CONFIG.delayTime;
			sendFlag = BeastSendMsgType.ESCAPE_END;
			clearBeastTimes();
		}
		beastGlobalDao.update(global);
		monsterVO.clear();
		BeastPushHelper.pushAllBeastAttacker(playerSession.onlineActorList(), (byte)0, (byte)percent);
	}
	
	/**
	 * 创建boss
	 */
	private void createBoss(int bossConfigId) {
		BeastMonsterConfig cfg = BeastService.BEAST_MONSTER_CONFIG;
		int maxLevel = actorDao.getMaxLevelActorId(Game.getServerId());
		monsterVO = BeastService.getBoss(maxLevel);
		BeastGlobal global = beastGlobalDao.get(bossConfigId);
		int extraHp = cfg.getExtraHp(global.lastHurt, global.bossDeadUseTime);
		for (MonsterVO vo : monsterVO.values()) {
			if (extraHp > 0) {
				vo.setHp(extraHp);
				vo.setMaxHp(extraHp);
			}
		}
	}
	
	@Override
	public TResult<BeastStatusResponse> getStatus(long actorId) {
		byte status = 0;
		byte percent = 0;
		BeastStatusResponse response = new BeastStatusResponse(status, percent);
		boolean isInTime = BeastService.isInActBbossTime();
		if (isInTime == false) {
			//boss时间已经过了
			return TResult.sucess(response);
		}
		TResult<Byte> result = this.getBloodPrecent();
		if (result.isOk()) {
			response.bloodPer = result.item.byteValue();
		} else {
			return TResult.sucess(response);
		}
		if (response.bloodPer != 0) {
			response.status = 1;
		}
		return TResult.sucess(response);
	}
	
	private MonsterVO getBoss() {
		MonsterVO boss = null;
		for (MonsterVO vo : monsterVO.values()) {
			boss = vo;
		}
		return boss;
	}

	@Override
	public TResult<BeastInfoResponse> getInfo(long actorId) {
		Result check = this.baseCondtionCheck(actorId);
		if (check.isFail()) {
			return TResult.valueOf(check.statusCode);
		}
		Beast beast = beastDao.get(actorId);
		int now = TimeUtils.getNow();
		int actCount = beast.ackTimes;
		int configCD = BeastService.getCountDownTime();
		int divice =  Math.min(configCD, Math.abs(now - beast.lastAckTime));
		int CDTime = divice > configCD? 0 : configCD - divice;
		if (beast.lastAckTime == 0) {
			CDTime = 0;
		}
		int cd = BeastService.getActivityEndTime() - now;
		int countdown = Math.max(0, cd);
		if (beastActorList.contains(actorId) == false) {
			beastActorList.add(actorId);
		}
		BeastInfoResponse info = new BeastInfoResponse(actCount, countdown, CDTime);
		return TResult.sucess(info);
	}

	@Override
	public TResult<Byte> getBloodPrecent() {
//		boolean isInTime = BeastService.isInActBbossTime();
//		if (isInTime == false) {
//			//boss时间已经过了
//			return TResult.valueOf(GameStatusCodeConstant.BEAST_TIME_OUT);
//		}
		if (monsterVO.isEmpty()) {
			//boss不见了
			return TResult.valueOf(GameStatusCodeConstant.BEAST_BOSS_DEAD);
		}
		ChainLock lock = LockUtils.getLock(monsterVO);
		byte precent = 0;
		float nowBlood = 0;
		float fullBlood = 0;
		try {
			lock.lock();
			MonsterVO boss = getBoss();
			nowBlood = (float)boss.getHp();
			fullBlood = (float)boss.getMaxHp();
			precent = (byte)Math.ceil(nowBlood / fullBlood * 100);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		if (nowBlood > 0) {
			precent = (precent == 0?1 : precent);
		}
		return TResult.sucess(precent);
	}

	@Override
	public Result attack(long actorId, boolean useTicket) {
		Result check = this.baseCondtionCheck(actorId);
		if (check.isFail()) {
			return Result.valueOf(check.statusCode);
		}
		boolean isBossDead = false;
		TResult<Byte> result = this.getBloodPrecent();
		byte precent = (byte)0;
		if (result.isOk()) {
			precent = result.item.byteValue();
		} else {
			return Result.valueOf(result.statusCode);
		}
		if(precent == 0) {
			//boss si le 
			isBossDead = true;
			return Result.valueOf(GameStatusCodeConstant.BEAST_BOSS_DEAD);
		}
		
		Beast beast = beastDao.get(actorId);
		int cdTime = BeastService.BEAST_GLOBAL_CONFIG.coolDownTime;
		int lastActTime = beast.lastAckTime;
		int needCostTicket = 0;
		boolean cdRunout = Math.abs(TimeUtils.getNow() - lastActTime) > cdTime;
		if (useTicket == true) {
			if (cdRunout == false) {
				int ticketNum = BeastService.BEAST_GLOBAL_CONFIG.costTicket;
				boolean hasEnough = vipFacade.hasEnoughTicket(actorId, ticketNum);
				if (hasEnough == false) {
					//钱不够
					return Result.valueOf(GameStatusCodeConstant.TICKET_NOT_ENOUGH);
				}
				if (isBossDead == true) {
					//boss死了
					return Result.valueOf(GameStatusCodeConstant.BEAST_BOSS_DEAD);
				}
				needCostTicket = ticketNum;
//				vipFacade.decreaseTicket(actorId, TicketDecreaseType.BEAST, ticketNum, 0, 0);
			}
		} else {
			if (cdRunout == false) {
				//cd冷却
				return Result.valueOf(GameStatusCodeConstant.BEAST_IN_CD);
			}
		}
		if (isBossDead == true) {
			//boss死了
			return Result.valueOf(GameStatusCodeConstant.BEAST_BOSS_DEAD);
		}
		LineupFightModel atkLineup = LineupHelper.getLineupFight(actorId);
		Map<Integer, MonsterVO> defTeam = monsterVO; // 拿boss
		MapConfig fightMap = MapService.get(BeastService.BEAST_GLOBAL_CONFIG.mapId);
		Actor actor = actorFacade.getActor(actorId);
		AttackMonsterRequest attackMonsterRequest = new AttackMonsterRequest(EventKey.LEARN_BATTLE, fightMap, actorId, atkLineup, defTeam, actor.morale, BeastService.BEAST_MONSTER_CONFIG.getMorale(), useTicket,
				BattleType.BEAST);
		attackMonsterRequest.skipFirstRound = true;
		HashMap<String, Integer> args = new HashMap<String, Integer>();
		args.put("needCostTicket", needCostTicket);
		attackMonsterRequest.args = args;
		Result battleResult = battleFacade.submitAtkMonsterRequest(attackMonsterRequest, this);
		if (battleResult.isOk()) {
			return Result.valueOf();
		}
		return Result.valueOf(GameStatusCodeConstant.BATTLE_REQUEST_SUBMIT_FAIL);
	}

	private Result baseCondtionCheck(long actorId) {
		int actorLevel = ActorHelper.getActorLevel(actorId);
		if (actorLevel < BeastService.BEAST_GLOBAL_CONFIG.openLv) {
			//等级不够吧
			return Result.valueOf(GameStatusCodeConstant.BEAST_LEVEL_LIMIT);
		}
		
		boolean isInTime = BeastService.isInActBbossTime();
		if (isInTime == false) {
			//boss时间已经过了
			return Result.valueOf(GameStatusCodeConstant.BEAST_TIME_OUT);
		}
		return Result.valueOf();
	}
	@Override
	public void execute(BattleResult result) {
		AttackMonsterRequest attackMonsterRequest = (AttackMonsterRequest) result.battleReq;
		long actorId = attackMonsterRequest.actorId;
		boolean isFinalBlow = false;
		@SuppressWarnings("unchecked")
		HashMap<String, Integer> attackMap = (HashMap<String, Integer>)attackMonsterRequest.args;
		ChainLock lock = LockUtils.getLock(monsterVO);
		try {
			lock.lock();
			MonsterVO boss = getBoss();
			int nowBlood = boss.getHp();
			if (nowBlood <= 0) {
				BeastPushHelper.attackBossFail(actorId, Result.valueOf(GameStatusCodeConstant.BEAST_BOSS_DEAD));
				BeastPushHelper.pushBeastAttacker(actorId, (byte)0, (byte)0);
				return;
			}
			vipFacade.decreaseTicket(actorId, TicketDecreaseType.BEAST, attackMap.get("needCostTicket"), 0, 0);
			Fighter bossFighter = result.defendsTeam.get(0);
			if (bossFighter.getHeroId() == boss.getHeroId()) {
				int hp = boss.getHp() - bossFighter.getHert();
				hp = hp < 0 ? 0 : hp;
				boss.setHp(hp);
				if (hp <= 0) {
					isFinalBlow = true;
				}
			}
			BeastGlobal global = beastGlobalDao.get(BeastService.BEAST_GLOBAL_CONFIG.monsterConfigId);
			if (boss.getHp() <= 0) {
				global.bossDeadUseTime = TimeUtils.getNow();
			}
			global.lastHurt = global.lastHurt + bossFighter.getHert();
			beastGlobalDao.update(global);
			
			float per = ((float)boss.getHp() / (float)boss.getMaxHp());
			float oldPer = ((float)nowBlood / (float)boss.getMaxHp());
			byte oldPercent = (byte)(oldPer * 100);
			byte newPercent = (byte)Math.ceil(per * 100);
			int status = boss.getHp() > 0? 1 : 0;
			if (oldPercent > newPercent) {
				BeastPushHelper.pushAllBeastAttacker(beastActorList, (byte)status, newPercent);
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		
		Beast beast = beastDao.get(actorId);
		ChainLock beastLock = LockUtils.getLock(beast);
		try {
			beastLock.lock();
			beast.lastAckTime = TimeUtils.getNow();
			beast.ackTimes++;
			beast.increaLeastCount();
			beastDao.update(beast);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			beastLock.unlock();
		}
		List<RewardObject> rewardList = BeastService.getRewardList(isFinalBlow, beast, ActorHelper.getActorLevel(actorId));
		sendReward(actorId, rewardList);
		Long exp = sendReputation(actorId, beast);
		BeastAttackResponse beastAttackResponse = new BeastAttackResponse(exp.longValue(), result.fightData, rewardList, BeastService.BEAST_GLOBAL_CONFIG.coolDownTime);
		BeastPushHelper.pushBeastBattleResponse(actorId, beastAttackResponse);
		
		if (isFinalBlow == true) {
			end(BeastEndType.BEAT);
			LOGGER.info(String.format("年兽boss被击杀,  actorId:[%s], 击杀时间[%s].",actorId, DateUtils.formatTime(new Date(System.currentTimeMillis()))));
			//找出大奖
			EquipConfig config = EquipService.get(rewardList.get(0).id);
			String msg = String.format(BeastService.BEAST_GLOBAL_CONFIG.beatEnd, ActorHelper.getActorName(actorId), config.getName());
			maintainFacade.sendNotice(msg, 1, 0, new ArrayList<Integer>());
		}
	}
	
	private Long sendReputation(long actorId, Beast beast) {
		Long value = 0L;
		if (beast.ackTimes <= BeastService.BEAST_GLOBAL_CONFIG.getReputationTimes) {
			Actor actor = actorFacade.getActor(actorId);
			ActorUpgradeConfig config = ActorService.getUpgradeConfig(actor.level);
			long needReputation = config.getNeedReputation();
			value = (needReputation * BeastService.BEAST_GLOBAL_CONFIG.getReputationPrecent / 1000);
			boolean result = actorFacade.addReputation(actorId, ReputationAddType.BEAST, value);
			if (result == false) {
				return 0L;
			}
		}
		return value;
	}

	/**
	 * 发放奖励
	 * 
	 * @param actorId
	 * @param id
	 * @param num
	 * @param rewardType
	 */
	private void sendReward(long actorId, int id, int num, RewardType rewardType) {
		switch (rewardType) {
		case EQUIP: {
			for (int i = 0; i < num; i++) {
				equipFacade.addEquip(actorId, EquipAddType.BEAST, id);
			}
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.BEAST, id, num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.BEAST, id, num);
			break;
		}

		default:
			LOGGER.error(String.format("类型错误，type:[%s]", rewardType.getCode()));
			break;
		}
	}

	/**
	 * 发放奖励
	 * 
	 * @param actorId
	 * @param list
	 */
	private void sendReward(long actorId, List<RewardObject> list) {
		for (RewardObject rewardObject : list) {
			sendReward(actorId, rewardObject.id, rewardObject.num, rewardObject.rewardType);
		}
	}

	@Override
	public void onZero() {
		Set<Long> actorIds = playerSession.onlineActorList();
		for (Long actorId : actorIds) {// 清除在线玩家试炼信息
			Beast beast = beastDao.get(actorId);
			ChainLock lock = LockUtils.getLock(beast);
			try{
				lock.lock();
				beast.ackTimes = 0;
				beastDao.update(beast);
			}catch(Exception e){
				LOGGER.error(String.format("{onZero : reset Beast ackTimes error actorId[%s]}", actorId),e);
			}finally{
				lock.unlock();
			}
		}
	}
	
	public void clearBeastTimes() {
		Set<Long> actorIds = playerSession.onlineActorList();
		for (Long actorId : actorIds) {// 清除在线玩家试炼信息
			Beast beast = beastDao.get(actorId);
			ChainLock lock = LockUtils.getLock(beast);
			try{
				lock.lock();
				beast.ackTimes = 0;
				beastDao.update(beast);
			}catch(Exception e){
				LOGGER.error(String.format("{onBeastActivityEnd : reset Beast ackTimes error actorId[%s]}", actorId),e);
			}finally{
				lock.unlock();
			}
		}
	}

	@Override
	public void onLogin(long actorId) {
		if (BeastService.isInActBbossTime() == true) {
			return;
		}
		Beast beast = beastDao.get(actorId);
		ChainLock lock = LockUtils.getLock(beast);
		try{
			lock.lock();
			beast.ackTimes = 0;
			beastDao.update(beast);
		}catch(Exception e){
			LOGGER.error(String.format("{onLogin : reset Beast ackTimes error actorId[%s]}", actorId),e);
		}finally{
			lock.unlock();
		}
	}
}
