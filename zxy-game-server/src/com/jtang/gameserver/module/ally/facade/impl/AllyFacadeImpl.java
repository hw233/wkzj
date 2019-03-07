package com.jtang.gameserver.module.ally.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.ALLY_ACTOR_NOT_ONLINE;
import static com.jiatang.common.GameStatusCodeConstant.ALLY_ACTOR_UNEXISTS;
import static com.jiatang.common.GameStatusCodeConstant.ALLY_ALREADY_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.ALLY_CAN_NOT_ADD_SELF;
import static com.jiatang.common.GameStatusCodeConstant.ALLY_COORDINATE_UNEXISTS;
import static com.jiatang.common.GameStatusCodeConstant.ALLY_DAY_FIGHT_NUM_LIMIT;
import static com.jiatang.common.GameStatusCodeConstant.ALLY_STORY_LOCK;
import static com.jiatang.common.GameStatusCodeConstant.ALLY_SUM_LIMIT;
import static com.jiatang.common.GameStatusCodeConstant.ALLY_UNEXISTS;
import static com.jiatang.common.GameStatusCodeConstant.ALL_ADD_ACTOR_FAIL;
import static com.jiatang.common.GameStatusCodeConstant.FIGHT_ENERGE_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.FIGHT_TIMES_REACH_LIMIT;
import static com.jiatang.common.GameStatusCodeConstant.OPPOSITE_ALLY_SUM_LIMIT;
import static com.jiatang.common.GameStatusCodeConstant.REPEAT_ALLY_TIME_LIMIT;
import static com.jiatang.common.GameStatusCodeConstant.TARGET_ALLY_STORY_LOCK;
import static com.jiatang.common.GameStatusCodeConstant.VIT_NOT_ENOUGH;
import static com.jtang.gameserver.module.ally.constant.AllyRule.ADD_ALLY_STORY_LOCK;
import static com.jtang.gameserver.module.ally.constant.AllyRule.ALLY_ALLIES_LIMIT;
import static com.jtang.gameserver.module.ally.constant.AllyRule.ALLY_FIGHT_COUNT_RESET_TIME;
import static com.jtang.gameserver.module.ally.constant.AllyRule.ALLY_FIGHT_MAP_ID;
import static com.jtang.gameserver.module.ally.constant.AllyRule.ALLY_FIGHT_NEED_ENERGY;
import static com.jtang.gameserver.module.ally.constant.AllyRule.ALLY_FIGHT_NUM_LIMIT;
import static com.jtang.gameserver.module.ally.constant.AllyRule.ALLY_FIGHT_REWARD;
import static com.jtang.gameserver.module.ally.constant.AllyRule.ALLY_FIGHT_REWARD_MIN_LEVEL_DIFFER;
import static com.jtang.gameserver.module.ally.constant.AllyRule.ALLY_REPEAT_BEYOND_TIME;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.component.event.ActorLevelUpEvent;
import com.jtang.gameserver.component.event.AddAllyEvent;
import com.jtang.gameserver.component.event.AllyPKEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.listener.ActorLogoutListener;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.dataconfig.service.ActorService;
import com.jtang.gameserver.dataconfig.service.GmService;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Ally;
import com.jtang.gameserver.module.ally.constant.AllyRule;
import com.jtang.gameserver.module.ally.dao.AllyCoordinateDao;
import com.jtang.gameserver.module.ally.dao.AllyDao;
import com.jtang.gameserver.module.ally.facade.AllyFacade;
import com.jtang.gameserver.module.ally.handler.response.GetActorsResponse;
import com.jtang.gameserver.module.ally.helper.AllyPushHelper;
import com.jtang.gameserver.module.ally.model.ActorVO;
import com.jtang.gameserver.module.ally.model.AllyVO;
import com.jtang.gameserver.module.ally.model.CoordinateVO;
import com.jtang.gameserver.module.ally.type.AllyAttributeKey;
import com.jtang.gameserver.module.battle.constant.BattleSkipPlayType;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackPlayerRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.buffer.facade.BufferFacade;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.story.facade.StoryFacade;
import com.jtang.gameserver.module.user.dao.ActorDao;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.EnergyDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 
 * @author pengzy
 * 
 */
@Component
public class AllyFacadeImpl implements AllyFacade, ActorLoginListener, ActorLogoutListener, Receiver, BattleCallBack {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	public static final long ROBOT_ID = 101L;
	@Autowired
	private AllyDao allyDao;
	@Autowired
	private AllyCoordinateDao coordinateDao;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private BattleFacade battleFacade;
	@Autowired
	private LineupFacade lineupFacade;
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private BufferFacade bufferFacade;
	@Autowired
	private NotifyFacade notifyFacade;
	@Autowired
	private EventBus eventBus;
	@Autowired
	private ActorDao actorDao;
	@Autowired
	private StoryFacade storyFacade;
	@Autowired
	private IconFacade iconFacade;

	@Override
	public Collection<AllyVO> getAlly(long actorId) {
		Ally ally = allyDao.get(actorId);
		Collection<AllyVO> allyCol = ally.getAlly();
		for (AllyVO allyVO : allyCol) {
			Actor actor = actorFacade.getActor(allyVO.actorId);
			allyVO.name = actor.actorName;
			allyVO.level = actor.level;
			allyVO.isOnline = playerSession.isOnline(actor.getPkId()) == false ? 0 : 1;
			allyVO.vipLevel = vipFacade.getVipLevel(allyVO.actorId);
			allyVO.iconVO = iconFacade.getIconVO(allyVO.actorId);
			if (DateUtils.isTime4DailyEvent(ALLY_FIGHT_COUNT_RESET_TIME, allyVO.fightTime)) {
				resetAllyFightCount(ally, allyVO);
			}
		}
		return allyCol;
	}

	@Override
	public Result addAlly(long actorId, long allyActorId) {
		if (actorId == allyActorId) {
			return Result.valueOf(ALLY_CAN_NOT_ADD_SELF);
		}
		if(allyActorId != ROBOT_ID){
			Result result = storyFacade.checkBattle(actorId,ADD_ALLY_STORY_LOCK);
			if(result.isFail()){
				return Result.valueOf(ALLY_STORY_LOCK);
			}
			result = storyFacade.checkBattle(allyActorId, ADD_ALLY_STORY_LOCK);
			if(result.isFail()){
				return Result.valueOf(TARGET_ALLY_STORY_LOCK);
			}
		}
		Result check = checkActor(allyActorId);
		if (check.isFail()) {
			return check;
		}
		Ally targetAlly = allyDao.get(allyActorId);
		ChainLock targetLock = LockUtils.getLock(targetAlly);
		try {
			targetLock.lock();
			if (targetAlly.containAlly(actorId)) {
				return Result.valueOf(ALLY_ALREADY_EXISTS);
			}
			if (targetAlly.getAllySize() >= ALLY_ALLIES_LIMIT && allyActorId != 101L) {
				return Result.valueOf(OPPOSITE_ALLY_SUM_LIMIT);
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			targetLock.unlock();
		}

		Ally myAlly = allyDao.get(actorId);
		if (myAlly.containAlly(allyActorId)) {
			return Result.valueOf(ALLY_ALREADY_EXISTS);
		}
		if (myAlly.getAllySize() >= ALLY_ALLIES_LIMIT) {
			return Result.valueOf(ALLY_SUM_LIMIT);
		}

		if (myAlly.containRemoveAlly(allyActorId)) {
			if (!myAlly.removeAllyBeyondTheTime(allyActorId, ALLY_REPEAT_BEYOND_TIME)) {
				return Result.valueOf(REPEAT_ALLY_TIME_LIMIT);
			}
			myAlly.removeOverdueRemoveAlly(ALLY_REPEAT_BEYOND_TIME);
			targetAlly.removeOverdueRemoveAlly(ALLY_REPEAT_BEYOND_TIME);
		}
		// 产生我的结盟数据
		generateAlly(actorId, allyActorId, myAlly);
		if(allyActorId != ROBOT_ID){
			// 产生被加盟友的相关数据
			generateAlly(allyActorId, actorId, targetAlly);
		}
		
		eventBus.post(new AddAllyEvent(actorId, myAlly.getAllySize(), allyActorId, targetAlly.getAllySize()));
		
		notifyFacade.createAddAlly(actorId, allyActorId);
		
		Actor actor = actorFacade.getActor(actorId);
		GameOssLogger.allyAdd(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, allyActorId);
		
		return Result.valueOf();
	}

	/**
	 * 生成盟友数据
	 * @param actorId
	 * @param allyActorId	盟友Id
	 * @param ally			我的盟友列表
	 *            
	 */
	private void generateAlly(long actorId, long allyActorId, Ally ally) {
		Actor allyActor = actorFacade.getActor(allyActorId);
		int vipLevel = vipFacade.getVipLevel(allyActorId);
		IconVO iconVO = iconFacade.getIconVO(allyActorId);
		AllyVO allyVO = AllyVO.valueOf(allyActorId, allyActor.actorName, allyActor.level, playerSession.isOnline(allyActorId) ? 1 : 0, vipLevel,iconVO);
		ally.addAlly(allyVO);
		allyDao.update(ally);
		if (playerSession.isOnline(actorId)) {
			AllyPushHelper.pushAddAlly(actorId, allyVO);
		}
	}

	@Override
	public Result removeAlly(long actorId, long allyActorId) {
		//TODO 有获取双方的数据。最好是加锁来处理这个问题。
		Ally myAlly = allyDao.get(actorId);
		if (!myAlly.containAlly(allyActorId)) {
			return Result.valueOf(ALLY_ACTOR_UNEXISTS);
		}
		myAlly.removeAlly(allyActorId);
		allyDao.update(myAlly);

		Ally oppositeAlly = allyDao.get(allyActorId);
		oppositeAlly.removeAlly(actorId);
		allyDao.update(oppositeAlly);

		// 发送系统消息给对方,提示被加为盟友
		if (playerSession.isOnline(allyActorId)) {
			AllyPushHelper.pushRemoveAlly(allyActorId, actorId);
		}
		AllyPushHelper.pushRemoveAlly(actorId, allyActorId);
//		eventBus.post(new RemoveAllyEvent(actorId, allyActorId));

		notifyFacade.createRemoveAlly(actorId, allyActorId);
		
		Actor actor = actorFacade.getActor(actorId);
		GameOssLogger.allyDel(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, allyActorId);
		
		return Result.valueOf();
	}

	@Override
	public int getAllyLevel(long actorId) {
		Collection<AllyVO> ally = getAlly(actorId);
		if (ally == null) {
			return 0;
		}
		int allyLevel = 0;
		for (AllyVO vo : ally) {
			Actor actor = actorFacade.getActor(vo.actorId);
			if (actor != null) {
				allyLevel += actor.level;
			}
		}
		return allyLevel;
	}

	@Override
	public boolean isAlly(long actorId, long allyActorId) {
		Ally ally = allyDao.get(actorId);
		return ally.containAlly(allyActorId);
	}

	@Override
	public Result fight(long actorId, long allyActorId) {
		Ally ally = allyDao.get(actorId);
		Actor actor = actorFacade.getActor(actorId);
		if (actor.energy < ALLY_FIGHT_NEED_ENERGY) {
			return Result.valueOf(FIGHT_ENERGE_NOT_ENOUGH);
		}

		if (!ally.containAlly(allyActorId)) {
			return Result.valueOf(ALLY_UNEXISTS);
		}
		if (Game.skillDebug() == false) {
			if (checkDayFightCount(ally)) {
				return Result.valueOf(ALLY_DAY_FIGHT_NUM_LIMIT);
			}
		}

		AllyVO allyVO = ally.getAlly(allyActorId);

		if (checkAllyFightCount(ally, allyVO)) {
			return Result.valueOf(FIGHT_TIMES_REACH_LIMIT);
		} 
		
		
		
		// 扣除精力值
		if (ALLY_FIGHT_NEED_ENERGY > 0) {
			boolean isOk = actorFacade.decreaseEnergy(actorId, EnergyDecreaseType.ALLY_FIGHT, ALLY_FIGHT_NEED_ENERGY);
			if (!isOk) {
				return Result.valueOf(VIT_NOT_ENOUGH);
			}
		}
		
		Actor allyActor = actorFacade.getActor(allyActorId);
		
		// 构建战斗
		LineupFightModel atkLineup = LineupHelper.getLineupFight(actorId);
		LineupFightModel defLineup = LineupHelper.getLineupFight(allyActorId);

		MapConfig fightMap = MapService.get(ALLY_FIGHT_MAP_ID);
		AttackPlayerRequest attackReq = new AttackPlayerRequest(EventKey.LEARN_BATTLE, fightMap, actorId, atkLineup, allyActorId, defLineup,
				actor.morale, allyActor.morale, null, BattleType.ALLY);
		Result result = battleFacade.submitAtkPlayerRequest(attackReq, this);
		if (result.isFail()) {// 战斗没有开打,前期检查没有通过
			return Result.valueOf(result.statusCode);
		}		

		return result;
	}

	/**
	 * 检查与单个盟友得切磋次数是否达到上限，并重置切磋次数和时间如果需要
	 * 
	 * @param ally
	 * @param allyVO
	 * @return
	 */
	private boolean checkAllyFightCount(Ally ally, AllyVO allyVO) {
		if (DateUtils.isTime4DailyEvent(ALLY_FIGHT_COUNT_RESET_TIME, allyVO.fightTime)) {
			resetAllyFightCount(ally, allyVO);
		}
		return allyVO.fightNum >= ALLY_FIGHT_NUM_LIMIT;
	}

	private void resetAllyFightCount(Ally ally, AllyVO allyVO) {
		allyVO.fightNum = 0; 
		allyVO.fightTime = TimeUtils.getNow();
		allyDao.update(ally);
	}

	/**
	 * 检查日可切磋次数是否达到上限，并更新日切磋次数和时间如果需要的话
	 * 
	 * @param ally
	 * @return
	 */
	private boolean checkDayFightCount(Ally ally) {
		if (DateUtils.isTime4DailyEvent(ALLY_FIGHT_COUNT_RESET_TIME, ally.lastFightTime)) {
			resetDayFightCount(ally);
		}
		return ally.dayFightCount >= ally.getAllySize();
	}

	/**
	 * 注册切磋战斗事件，并会在onEvent中回调
	 */
	@PostConstruct
	public void init() {
		eventBus.register(EventKey.ACTOR_LEVEL_UP, this);
	}

	@Override
	public void onEvent(Event paramEvent) {		
		if (paramEvent.getName() == EventKey.ACTOR_LEVEL_UP) {
			ActorLevelUpEvent event = paramEvent.convert();
			actorLevelup(event.actor.getPkId(), event.actor.level);
		}
	}

	private void fightFinish(BattleResult battleResult) {
		AttackPlayerRequest attackReq = (AttackPlayerRequest) battleResult.battleReq;
		FightData data = battleResult.fightData;
		data.setCanSkipPlay(BattleSkipPlayType.PVP_CLIENT_DECIDE);
		Map<Long, Integer> rewards = null;
		Ally ally = allyDao.get(attackReq.actorId);
		AllyVO allyVO = ally.getAlly(attackReq.targetActorId);
		int rewardMorale = 0;
		
		/**
		 * 玩家掌教等级-对手掌教等级 > n，切磋无论任何结果都无气势奖励 玩家掌教等级-对手掌教等级 <=
		 * n，切磋大胜奖励3，胜利奖励2，险胜奖励1，失败奖励0（可配置） 1.每个盟友每天第一次切磋气势奖励翻倍
		 * 2.被切磋的盟友也会根据切磋结果奖励气势，只是没有气势翻倍的情况
		 */
		if (getLevelDiffer(attackReq.actorId, attackReq.targetActorId) <= ALLY_FIGHT_REWARD_MIN_LEVEL_DIFFER) {
			rewards = reward(data.result, attackReq.actorId, attackReq.targetActorId, isFirstTime(allyVO));
		}
		if (rewards != null && rewards.containsKey(attackReq.actorId)) {
			rewardMorale = rewards.get(attackReq.actorId);
		}

		AllyPushHelper.pushFightResult(attackReq.actorId, rewardMorale, data);

		if (data.result.isWin()) {
			updateFightResult(true, attackReq.actorId, attackReq.targetActorId);
		} else {
			updateFightResult(false, attackReq.targetActorId, attackReq.actorId);
		}

		if (allyVO != null && allyVO.actorId != ROBOT_ID) {
			notifyFacade.createAllyFight(attackReq.actorId, attackReq.targetActorId, data.result.isWin(), rewards, allyVO.winNum, allyVO.failNum,
					data.getBytes());
		}
		
		eventBus.post(new AllyPKEvent(attackReq.actorId, attackReq.targetActorId));
	}

	private boolean isFirstTime(AllyVO allyVO) {
		if (allyVO != null) {
			return allyVO.fightNum == 0;
		}
		return false;
	}

	private Map<Long, Integer> reward(WinLevel result, long actorId, long targetActorId, boolean isFirstFight) {
		Map<Long, Integer> rewards = new HashMap<>(2);
		int morale = ALLY_FIGHT_REWARD.get(result);
		if (isFirstFight) {
			morale = morale * 2;
		}
		if (morale > 0) {
			actorFacade.addMorale(actorId, morale);
		}
		rewards.put(actorId, morale);
		morale = ALLY_FIGHT_REWARD.get(result);
		if (morale > 0) {
			actorFacade.addMorale(targetActorId, morale);
		}
		rewards.put(targetActorId, morale);
		return rewards;
	}

	private int getLevelDiffer(long actorId, long targetActorId) {
		Actor actor = actorFacade.getActor(actorId);
		Actor targetActor = actorFacade.getActor(targetActorId);
		return Math.abs(actor.level - targetActor.level);
	}

	/**
	 * 更新盟友切磋后相关的AllyVO，切磋时间、切磋次数、切磋胜负数等字段
	 * 
	 * @param isAtker
	 *            是否为攻击者，非攻击者不需更新其日切磋次数等
	 * @param winnerActorId
	 * @param failerActorId
	 */
	private void updateFightResult(boolean isAtker, long winnerActorId, long failerActorId) {
		Ally winnerAlly = allyDao.get(winnerActorId);
		Ally failerAlly = allyDao.get(failerActorId);
		updateAllyData(isAtker, true, winnerAlly, failerActorId);
		updateAllyData(!isAtker, false, failerAlly, winnerActorId);
	}

	private void updateAllyData(boolean isAtker, boolean isWin, Ally myAlly, long allyActorId) {
		AllyVO allyVO = getAllyVO(myAlly, allyActorId);
		if (allyVO == null) {
			return;
		}
		
		// 更改与盟友得切磋次数
		if (isAtker) {
			allyVO.fightNum += 1;
			// allyVO.setFightCounter(allyVO.getFightCounter() + 1);
			allyVO.fightTime = TimeUtils.getNow();
		}

		// 更改胜负次数
		if (isWin) {
			allyVO.winNum += 1;  //allyVO.setVictoryCounter(allyVO.getVictoryCounter() + 1);
		} else {
			allyVO.failNum += 1; //allyVO.setFailCounter(allyVO.getFailCounter() + 1);
		}
		// 更改日可切磋次数
		if (isAtker) {
			myAlly.dayFightCount += 1;  // (myAlly.dayFightCount + 1);
			myAlly.lastFightTime = TimeUtils.getNow();
		}
		allyDao.update(myAlly);

		// 若角色在线,则更新其数据
		if (playerSession.isOnline(myAlly.getPkId())) {
			Map<AllyAttributeKey, Object> attributeMap = getNewAllyAttributeMap(isAtker, isWin, allyVO);
			AllyPushHelper.pushAllyAttribute(myAlly.getPkId(), allyActorId, attributeMap);
			if (isAtker) {
				AllyPushHelper.pushAllyFightCount(myAlly.getPkId(), myAlly.dayFightCount);
			}
		}
	}

	private Map<AllyAttributeKey, Object> getNewAllyAttributeMap(boolean isAtker, boolean isWin, AllyVO allyVO) {
		Map<AllyAttributeKey, Object> attributeMap = new HashMap<>();
		if (isAtker) {
			attributeMap.put(AllyAttributeKey.FIGHT_TIME, allyVO.fightTime);
			attributeMap.put(AllyAttributeKey.FIGHT_NUM, allyVO.fightNum);
		}
		if (isWin) {
			attributeMap.put(AllyAttributeKey.WIN_NUM, allyVO.winNum);
		} else {
			attributeMap.put(AllyAttributeKey.FAIL_NUM, allyVO.failNum);
		}
		return attributeMap;
	}

	private AllyVO getAllyVO(Ally ally, long allyActorId) {
		AllyVO allyVO = ally.getAlly(allyActorId);
		if (allyVO != null) {
			Actor actor = actorFacade.getActor(allyActorId);
			allyVO.name = actor.actorName;
			allyVO.level = actor.level;
		}
		return allyVO;
	}

	@Override
	public int getDayFightCount(long actorId) {
		Ally ally = allyDao.get(actorId);
		return ally.dayFightCount;
	}

	@Override
	public int getCountDown(long actorId) {
		Ally ally = allyDao.get(actorId);
		int countDownSeconds = 0;// 重置倒计时时间
		/**
		 * 今天的重置时间
		 */
		Calendar resetClendar = DateUtils.getSpecialTimeOfToday(ALLY_FIGHT_COUNT_RESET_TIME, 0, 0, 0);
		int resetTime = (int) (resetClendar.getTimeInMillis() / 1000);

		/**
		 * 当前时间
		 */
		int currentTime = (int) (System.currentTimeMillis() / 1000);
		if (before(currentTime, resetTime)) {// 当前登录时间在重置时间之前
			countDownSeconds = resetTime - currentTime;
			resetClendar.add(Calendar.DAY_OF_YEAR, -1);
			int lastResetTime = (int) (resetClendar.getTimeInMillis() / 1000);// 上次可重置时间
			if (before(ally.lastFightTime, lastResetTime)) {// 如果最近一次切磋时间在上次可重置时间之前，则需要重置日可切磋次数和时间
				resetDayFightCount(ally);
			}
		} else {
			countDownSeconds = 24 * 60 * 60 - (currentTime - resetTime);
			if (before(ally.lastFightTime, resetTime)) {// 如果说当前时间超过了今日重置时间，并且上一次切磋时间在重置时间之前，则需重置
				resetDayFightCount(ally);
			}
		}
		return countDownSeconds;
	}

	private boolean before(int time1, int time2) {
		return time1 < time2;
	}

	private void resetDayFightCount(Ally ally) {
		ally.dayFightCount = 0;
		ally.lastFightTime = TimeUtils.getNow();
		allyDao.update(ally);
	}

	@Override
	public void onLogout(long actorId) {
		Collection<AllyVO> allAlly = getAlly(actorId);
		for (AllyVO allyVO : allAlly) {
			if (allyVO != null) {
				if (playerSession.isOnline(allyVO.actorId)) {// 盟友在线
					Map<AllyAttributeKey, Object> attributeMap = new HashMap<>();
					attributeMap.put(AllyAttributeKey.IS_ONLINE, (byte)0);
					AllyPushHelper.pushAllyAttribute(allyVO.actorId, actorId, attributeMap);// 通知盟友角色下线
				}
			}
		}
	}

	@Override
	public void onLogin(long actorId) {
		Collection<AllyVO> allAlly = getAlly(actorId);
		for (AllyVO allyVO : allAlly) {
			if (allyVO != null) {
				if (playerSession.isOnline(allyVO.actorId)) {// 盟友在线
					Map<AllyAttributeKey, Object> attributeMap = new HashMap<>();
					attributeMap.put(AllyAttributeKey.IS_ONLINE, (byte)1);
					AllyPushHelper.pushAllyAttribute(allyVO.actorId, actorId, attributeMap);// 通知盟友角色下线
				}
			}
		}
	}

	@Override
	public void actorLevelup(long actorId, int level) {
		Collection<AllyVO> allAlly = getAlly(actorId);
		for (AllyVO allyVO : allAlly) {
			if (allyVO != null) {
				if (playerSession.isOnline(allyVO.actorId)) {// 盟友在线
					Map<AllyAttributeKey, Object> attributeMap = new HashMap<>();
					attributeMap.put(AllyAttributeKey.LEVEL, level);
					AllyPushHelper.pushAllyAttribute(allyVO.actorId, actorId, attributeMap);// 通知盟友角色下线
				}
			}
		}
	}

	@Override
	public void updateCoordinate(long actorId, double longitude, double latitude, double height) {
		coordinateDao.update(actorId, longitude, latitude, height);
	}

	@Override
	public TResult<CoordinateVO> getCoordinate(long actorId) {
		Result check = checkActor(actorId);
		if (check.isFail()) {
			return TResult.valueOf(check.statusCode);
		}
		CoordinateVO coordinate = coordinateDao.get(actorId);
		if (coordinate == null) {
			return TResult.valueOf(ALLY_COORDINATE_UNEXISTS);
		}
		return TResult.sucess(coordinate);
	}
	
	private Result checkActor(long actorId) {
		boolean isExsit = actorFacade.isExists(actorId);
		if (!isExsit) {
			return Result.valueOf(ALLY_ACTOR_UNEXISTS);
		}
		if (!playerSession.isOnline(actorId) && actorId != ROBOT_ID) {
			return Result.valueOf(ALLY_ACTOR_NOT_ONLINE);
		}
		if(GmService.isGm(actorId)){
			return Result.valueOf(ALL_ADD_ACTOR_FAIL);
		}
		return Result.valueOf();
	}

	@Override
	public void execute(BattleResult result) {
		fightFinish(result);
	}
	
	@Override
	public TResult<GetActorsResponse> getActors(long actorId) {
		int levelLimit = ActorHelper.getActorLevel(actorId);
		Set<Long> actorIdSet = playerSession.onlineActorList();
		List<Long> actors = new ArrayList<>();
		for(Long actorIds:actorIdSet){
			Actor actor = actorFacade.getActor(actorIds);
			Ally targetAlly = allyDao.get(actorIds);
			boolean result1 = actor.level >= Math.max(levelLimit - AllyRule.FLUSH_ALLYS_LEVEL_LIMIT,0);
			boolean result2 = actor.level <= Math.min(levelLimit + AllyRule.FLUSH_ALLYS_LEVEL_LIMIT,ActorService.maxLevel());
			boolean result3 = targetAlly.containAlly(actorId) == false;
			boolean result4 = actorIds != actorId;
			boolean result5 = AllyRule.ALLY_ALLIES_LIMIT <= targetAlly.getAllySize();
			if(result1 && result2 && result3 && result4 && GmService.isGm(actorIds) == false && result5 == false){
				actors.add(actorIds);
			}
			if(AllyRule.FLUSH_ALLYS_LIST == actors.size()){//人数已满跳出
				break;
			}
		}
		
//		int minLevel = Math.max(levelLimit - AllyRule.FLUSH_ALLYS_LEVEL_LIMIT,0);
//		int maxLevel = Math.min(levelLimit + AllyRule.FLUSH_ALLYS_LEVEL_LIMIT,ActorService.maxLevel());
//		if(actors.size() == 0){
//			actors = actorDao.getActorIdList(minLevel, maxLevel, 1);
//		}
		
		if(actors.size() == 0){//数据库当前没有符合条件的数据,取固定一个死号给玩家加好友
			actors.add(ROBOT_ID);
		}
		
		List<ActorVO> actorList = new ArrayList<>();
		for(Long actorIds:actors){
			int vipLevel = vipFacade.getVipLevel(actorIds);
			Actor actor = actorFacade.getActor(actorIds);
			IconVO iconVO = iconFacade.getIconVO(actorIds);
			ActorVO actorVO = new ActorVO(actorIds,actor.actorName,actor.heroId,actor.level,vipLevel,iconVO);
			actorList.add(actorVO);
		}
		GetActorsResponse response = new GetActorsResponse(actorList);
		return TResult.sucess(response);
	}

	@Override
	public TResult<GetActorsResponse> getRobot(long actorId) {
		List<Long> actors = new ArrayList<>();
		actors.add(ROBOT_ID);
		List<ActorVO> actorList = new ArrayList<>();
		for(Long actorIds:actors){
			int vipLevel = vipFacade.getVipLevel(actorIds);
			Actor actor = actorFacade.getActor(actorIds);
			IconVO iconVO = iconFacade.getIconVO(actorIds);
			ActorVO actorVO = new ActorVO(actorIds,actor.actorName,actor.heroId,actor.level,vipLevel,iconVO);
			actorList.add(actorVO);
		}
		GetActorsResponse response = new GetActorsResponse(actorList);
		return TResult.sucess(response);
	}
}
