package com.jtang.gameserver.module.trialcave.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.BATTLE_CD_NOT_PASS;
import static com.jiatang.common.GameStatusCodeConstant.LEVEL_NOT_REACH;
import static com.jiatang.common.GameStatusCodeConstant.LINE_UP_EMPTY;
import static com.jiatang.common.GameStatusCodeConstant.TRIAL_COUNT_CAN_NOT_RESET;
import static com.jiatang.common.GameStatusCodeConstant.TRIAL_COUNT_RAN_OUT;
import static com.jiatang.common.GameStatusCodeConstant.TRIAL_RESET_COUNT_RAN_OUT;
import static com.jtang.core.protocol.StatusCode.TICKET_NOT_ENOUGH;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.event.EventBus;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.TrialBattleResultEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dataconfig.service.TrialCaveService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.TrialCave;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackMonsterRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.trialcave.dao.TrialCaveDao;
import com.jtang.gameserver.module.trialcave.facade.TrialCaveFacade;
import com.jtang.gameserver.module.trialcave.help.TrialPushHelper;
import com.jtang.gameserver.module.trialcave.model.TrialCaveInfoVO;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 试炼洞模块业务处理类
 * 
 * @author lig
 * 
 */
@Component
public class TrialCaveFacadeImpl implements TrialCaveFacade, BattleCallBack, ActorLoginListener, ZeroListener{

	private static final Logger LOGGER = LoggerFactory.getLogger(TrialCaveFacadeImpl.class);

	static String TRIAL_BATTTLE_ID = "TRIAL_BATTTLE_ID";

	@Autowired
	TrialCaveDao trialDao;
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	GoodsFacade goodsFacade;
	@Autowired
	LineupFacade lineupFacade;
	@Autowired
	BattleFacade battleFacade;
	@Autowired
	NotifyFacade notifyFacade;
	@Autowired
	VipFacade vipFacade;
	@Autowired
	PlayerSession playerSession;
	@Autowired
	Schedule schedule;
	@Autowired
	EventBus eventBus;

	@Override
	public TResult<TrialCave> getTrialCaveInfo(long actorId) {
		if (baseConditionCheck(actorId)) {
			TrialCave cave = this.trialDao.get(actorId);
			return TResult.sucess(cave);
		} else {
			return TResult.valueOf(LEVEL_NOT_REACH);
		}
	}

	private TrialCaveInfoVO getTrialCaveVO(long actorId, int entranceId) {
		TrialCave cave = this.trialDao.get(actorId);
		int alreadyTrialTimes = 0;
		int lastTrialTime = 0;

		if (entranceId == 1) {
			alreadyTrialTimes = cave.ent1trialed;
			lastTrialTime = cave.ent1LastAckTime;
		} else {
			alreadyTrialTimes = cave.ent2trialed;
			lastTrialTime = cave.ent2LastAckTime;
		}
		TrialCaveInfoVO vo = TrialCaveInfoVO.valueOf(entranceId, alreadyTrialTimes, lastTrialTime);
		return vo;
	}

	@Override
	public Result trialBattle(long actorId, int entranceId) {
		if (baseConditionCheck(actorId) == false) {
			return Result.valueOf(LEVEL_NOT_REACH);
		}
		TrialCaveInfoVO vo = getTrialCaveVO(actorId, entranceId);
		int interval = TrialCaveService.getIntervalTime();
		if (!DateUtils.beyondTheTime(vo.lastTrialTime, interval)) {
			return Result.valueOf(BATTLE_CD_NOT_PASS);
		}
		int usedTime = vo.alreadyTrialTimes;

		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		int maxTime = TrialCaveService.getVIPTrialNumByVIPLevel(vipPrivilege, entranceId);

		// 是否还有试炼次数
		if (usedTime >= maxTime) {
			return Result.valueOf(TRIAL_COUNT_RAN_OUT);
		}

		LineupFightModel atkTeam = LineupHelper.getLineupFight(actorId);
		// 开战
		MapConfig map = MapService.get(TrialCaveService.getMapId(entranceId));

		int lineupId = TrialCaveService.randomMonsterLineupId();

		Map<Integer, Integer> lineupMap = TrialCaveService.getMonsterLineupMap(lineupId);
		if (null == lineupMap) {
			return Result.valueOf(LINE_UP_EMPTY);
		}
		Map<Integer, MonsterVO> monsterMap = TrialCaveService.generateMonsters(lineupMap);
		int morale = actorFacade.getActor(actorId).morale;
		int monsterMorale = TrialCaveService.getMonsterLineupMorale(lineupId);

		Map<String, Object> args = new HashMap<String, Object>();
		args.put(TRIAL_BATTTLE_ID, entranceId);

		Map<Long, Map<AttackerAttributeKey, Integer>> defTeamAttrChange = new HashMap<>();// 怪物附加属性
		Actor actor = actorFacade.getActor(actorId);
		for (MonsterVO monsterVO : monsterMap.values()) {
			Map<AttackerAttributeKey, Integer> attributeMap = new HashMap<>();
			int hp = FormulaHelper.executeCeilInt(TrialCaveService.getMonsterHPExpr(), actor.level);
			int attack = FormulaHelper.executeCeilInt(TrialCaveService.getMonsterAttackExpr(), actor.level);
			int defense = FormulaHelper.executeCeilInt(TrialCaveService.getMonsterDefenseExpr(), actor.level);
			attributeMap.put(AttackerAttributeKey.HP, hp * monsterVO.getHp());
			attributeMap.put(AttackerAttributeKey.ATK, attack * monsterVO.getAtk());
			attributeMap.put(AttackerAttributeKey.DEFENSE, defense * monsterVO.getDefense());
			defTeamAttrChange.put(monsterVO.getSpriteId(), attributeMap);
		}
		
		AttackMonsterRequest event = new AttackMonsterRequest(EventKey.TRIAL_BATTLE, map, actorId, 
				atkTeam, monsterMap, morale, monsterMorale, null, defTeamAttrChange, args, BattleType.TRIAL);
		Result result = battleFacade.submitAtkMonsterRequest(event, this);

		// 战斗没有开打,前期检查没有通过.
		if (result.isFail()) {
			return Result.valueOf(result.statusCode);
		}

		TrialCave cave = this.trialDao.get(actorId);
		ChainLock lock = LockUtils.getLock(cave);
		try {
			lock.lock();
			cave.increaseTrialCaveCount((byte) entranceId);
			cave.lastTrialCheck(maxTime, entranceId);
			this.trialDao.update(cave);
		}catch(Exception e){
			LOGGER.error("{trialBattle : increase trialCount error}",e);
		}finally{
			lock.unlock();
		}
		// 扣除试炼次数
		return Result.valueOf();
	}

	@Override
	public TResult<Integer> resetTrialCave(long actorId) {
		if (baseConditionCheck(actorId) == false) {
			return TResult.valueOf(LEVEL_NOT_REACH);
		}
		TrialCave cave = trialDao.get(actorId);
		int oldCount = cave.trialResetCount;
		int nextCount = oldCount + 1;

		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		int resetNum = TrialCaveService.getResetNumByVIPLevel(vipPrivilege);
		// 根据vip等级获取vip可以重置的次数
		if (nextCount > resetNum) {
			// 试炼重置次数已用完
			return TResult.valueOf(TRIAL_RESET_COUNT_RAN_OUT);
		}

		int totalCount = cave.ent1trialed + cave.ent2trialed;
		int configCount = TrialCaveService.getVIPTrialNumByVIPLevel(vipPrivilege, 1) + TrialCaveService.getVIPTrialNumByVIPLevel(vipPrivilege, 2);
		if (totalCount < configCount) {
			return TResult.valueOf(TRIAL_COUNT_CAN_NOT_RESET);
		}
			
		int resetTicket = TrialCaveService.getResetCostByBuyTimes(cave.trialResetCount);

		if (resetTicket > 0) {
			boolean flag = vipFacade.decreaseTicket(actorId, TicketDecreaseType.BUY_TRIAL_CAVE_RESET, resetTicket, oldCount, nextCount);
			if (flag == false) {
				return TResult.valueOf(TICKET_NOT_ENOUGH);
			}
		}
		cave.trialCaveReset();
		this.trialDao.update(cave);

		Actor actor = actorFacade.getActor(actorId);
		GameOssLogger.trialReset(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, oldCount, nextCount);
		return TResult.sucess(cave.trialResetCount);
	}

	private boolean baseConditionCheck(long actorId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor.level >= TrialCaveService.getOpenLv()) {
			return true;
		}
		return false;
	}

	@Override
	public void onLogin(long actorId) {
		TrialCave cave = trialDao.get(actorId);
		if (DateUtils.isToday(cave.trialLastResetTime) == false) {
			ChainLock lock = LockUtils.getLock(cave);
			try{
				lock.lock();
				cave.trialCaveZeroUpdate();
//				int now = TimeUtils.getNow();
//				int endTime = cave.trialEndTime;
//				if (now > endTime) {
//					cave.totalTrialCount = 0;
//					cave.trialEndTime = (int)(TrialCaveService.getActivityEndTime() / 1000);
//				}
				trialDao.update(cave);
			}catch(Exception e){
				LOGGER.error("{onlogin : reset TrialCave trialResetCount error}",e);
			}finally{
				lock.unlock();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(BattleResult result) {
		long actorId = result.battleReq.actorId;
		Actor actor = actorFacade.getActor(actorId);
		// 获取战斗结果
		FightData fightData = result.fightData;
		Map<String, Object> args = (Map<String, Object>) result.battleReq.args;
		int entranceId = (int) args.get(TRIAL_BATTTLE_ID);
		WinLevel winLv = fightData.result;
		List<RewardObject> selfAwardGoods = null;
		
		boolean battleResult = winLv.isWin();
		TrialCave cave = trialDao.get(actorId);
		int trialCount = cave.totalTrialCount;
		selfAwardGoods = TrialCaveService.getRewardList(battleResult, entranceId, actor.level, trialCount);
		sendReward(actorId, selfAwardGoods);
		TrialCaveInfoVO vo = getTrialCaveVO(actorId, entranceId);
		TrialPushHelper.pushBattleResult(actorId, fightData, selfAwardGoods, vo);
		GameOssLogger.trialBattle(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, entranceId, winLv.getCode());
		eventBus.post(new TrialBattleResultEvent(actorId, vo.alreadyTrialTimes));
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
//		case GOLD: {
//			actorFacade.addGold(actorId, GoldAddType., num)(actorId, GoodsAddType.SPRINT_GIFT, id, num);
//			break;
//		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.TRAIL_AWARD, id, num);
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
			sendReward(actorId, rewardObject.id, rewardObject.num,rewardObject.rewardType);
		}
	}
	
	@Override
	public void dailyResetTrialCave(long actorId) {
		TrialCave trialCave = trialDao.get(actorId);
		trialCave.trialCaveFixTimeReset();
		trialDao.update(trialCave);
		TrialPushHelper.pushTrialedCount(actorId, trialCave.trialResetCount);
	}

	@Override
	public void onZero() {
		Set<Long> actorIds = playerSession.onlineActorList();
		int now = TimeUtils.getNow();
		for (Long actorId : actorIds) {// 清除在线玩家试炼信息
			TrialCave cave = trialDao.get(actorId);
			ChainLock lock = LockUtils.getLock(cave);
			try{
				lock.lock();
				cave.trialCaveZeroUpdate();
				cave.trialEndTime = now;
				trialDao.update(cave);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(String.format("重置试炼洞次数    actorId[%s], ent1trialed[%s], ent1LastAckTime[%s], ent2trialed[%s], ent2LastAckTime[%s], trialResetCount[%s], trialLastResetTime[%s]",
							actorId, cave.ent1trialed, cave.ent1LastAckTime, cave.ent2trialed, cave.ent2LastAckTime, cave.trialResetCount, cave.trialLastResetTime));
				}
			}catch(Exception e){
				LOGGER.error("{onZero : reset TrialCave trialResetCount error}",e);
			}finally{
				lock.unlock();
			}
			TrialPushHelper.pushTrialedCount(actorId, cave.trialResetCount);
		}
	}
}
