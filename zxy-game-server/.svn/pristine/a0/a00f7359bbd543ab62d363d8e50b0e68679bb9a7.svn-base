package com.jtang.gameserver.module.hole.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.ALLY_NOT_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.BATTLE_NOT_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.HOLE_ACCEPT_FIGHT;
import static com.jiatang.common.GameStatusCodeConstant.HOLE_ALLY_COUNT_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.HOLE_ALLY_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.HOLE_ALLY_NOT_INVITE;
import static com.jiatang.common.GameStatusCodeConstant.HOLE_CLOSE;
import static com.jiatang.common.GameStatusCodeConstant.HOLE_CONDITION_NOT_REACH;
import static com.jiatang.common.GameStatusCodeConstant.HOLE_INVITE_FAIL;
import static com.jiatang.common.GameStatusCodeConstant.HOLE_NOT_FIND;
import static com.jiatang.common.GameStatusCodeConstant.HOLE_OPEN;
import static com.jiatang.common.GameStatusCodeConstant.HOLE_REWARD_ACCEPT;
import static com.jiatang.common.GameStatusCodeConstant.NOTIFICATION_ALLY_NOT_FOUND;
import static com.jtang.core.protocol.StatusCode.DATA_VALUE_ERROR;
import static com.jtang.core.protocol.StatusCode.SUCCESS;
import static com.jtang.gameserver.module.hole.constant.HoleRule.HOLE_ALLY_COUNT;
import static com.jtang.gameserver.module.hole.constant.HoleRule.HOLE_TRIGGER_ALLY_COUNT;
import static com.jtang.gameserver.module.hole.constant.HoleRule.HOLE_TRIGGER_SELF_COUNT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
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
import com.jtang.gameserver.component.event.HoleBattleResultEvent;
import com.jtang.gameserver.component.event.StoryPassedEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.dataconfig.model.HoleBattleConfig;
import com.jtang.gameserver.dataconfig.model.HoleConfig;
import com.jtang.gameserver.dataconfig.model.HoleProportionConfig;
import com.jtang.gameserver.dataconfig.model.HoleRewardConfig;
import com.jtang.gameserver.dataconfig.service.HoleService;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dataconfig.service.MonsterService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Hole;
import com.jtang.gameserver.dbproxy.entity.HoleNotify;
import com.jtang.gameserver.dbproxy.entity.HoleTrigger;
import com.jtang.gameserver.module.adventures.vipactivity.facade.MainHeroFacade;
import com.jtang.gameserver.module.ally.facade.AllyFacade;
import com.jtang.gameserver.module.ally.model.AllyVO;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackMonsterRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.hole.constant.HoleRule;
import com.jtang.gameserver.module.hole.dao.HoleDao;
import com.jtang.gameserver.module.hole.dao.HoleNotifyDao;
import com.jtang.gameserver.module.hole.dao.HoleTriggerDao;
import com.jtang.gameserver.module.hole.facade.HoleFacade;
import com.jtang.gameserver.module.hole.handler.response.HoleFightResponse;
import com.jtang.gameserver.module.hole.handler.response.HoleResponse;
import com.jtang.gameserver.module.hole.help.HolePushHelp;
import com.jtang.gameserver.module.hole.model.HoleBattleResult;
import com.jtang.gameserver.module.hole.model.HoleNotifyVO;
import com.jtang.gameserver.module.hole.model.HoleVO;
import com.jtang.gameserver.module.hole.type.OpenType;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.story.constant.StoryRule;
import com.jtang.gameserver.module.story.helper.StoryHelper;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;
import com.jtang.gameserver.module.user.type.ReputationAddType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class HoleFacadeImpl implements HoleFacade, /*ApplicationListener<ContextRefreshedEvent>,*/ BattleCallBack, Receiver, ZeroListener, ActorLoginListener {

	private static final String BATTLEID = "BATTLEID";
	private static final String ID = "ID";
	private static final String HERO_MAP = "HERO_MAP";

	private static final Logger LOGGER = LoggerFactory.getLogger(HoleFacadeImpl.class);

	@Autowired
	private HoleDao holeDao;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private BattleFacade battleFacade;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private Schedule schedule;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private NotifyFacade notifyFacade;
	@Autowired
	private AllyFacade allyfacade;
	@Autowired
	private EventBus eventBus;
	@Autowired
	private MainHeroFacade mainHeroFacade;
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private HoleTriggerDao holeTriggerDao;
	
	@Autowired
	private HoleNotifyDao holeNotifyDao;

	@Override
	public HoleResponse getHoleResponse(long actorId) {
		List<Hole> holes = holeDao.getHoles(actorId);
		List<HoleVO> list = new ArrayList<HoleVO>();
		if (holes != null) {
			for (Hole hole : holes) {
				if (OpenType.ALLY.getCode() == hole.type) {
					String targetName = actorFacade.getActor(hole.ally).actorName;
					list.add(HoleVO.valueOf(hole, targetName));
				} else {
					list.add(HoleVO.valueOf(hole, ""));
				}
			}
		}
		
		HoleNotify notify = holeNotifyDao.get(actorId);
		HoleTrigger holeTrigger = holeTriggerDao.get(actorId);
		HoleResponse holeResponse = new HoleResponse(list, notify.getList(), holeTrigger.selfCount , holeTrigger.allyCount);
		return holeResponse;
	}

	@Override
	public TResult<List<RewardObject>> getReward(long id, long actorId) {
		Hole hole = holeDao.getHole(id, actorId);
		if (hole == null) {// 没有找到洞府
			return TResult.valueOf(HOLE_NOT_FIND);
		}


		Map<Integer, Integer> fightMap = hole.getFightsMap();
		if (fightMap.size() < HoleService.getHoleButtleNum(hole.holeId)) {// 没通关
			return TResult.valueOf(HOLE_CONDITION_NOT_REACH);
		}

		int minStar = 3;
		for (Integer key : fightMap.keySet()) {
			if (fightMap.get(key) < minStar) {
				minStar = fightMap.get(key);
			}
		}

		Map<Integer, Integer> rewardMap = hole.getRewardMap();
		HoleConfig holeConfig = HoleService.get(hole.holeId);
		List<RewardObject> list = new ArrayList<>();
		for (int i = minStar; i > 0; i--) {
			int isGet = rewardMap.get(i);
			if (isGet == 0) {
				HoleRewardConfig rewardConfig = holeConfig.getRewardConfig(i);
				RewardObject rewardObject = new RewardObject(RewardType.getType(rewardConfig.type), rewardConfig.id, rewardConfig.num);
				sendReward(actorId,rewardObject.rewardType, rewardObject.id, rewardObject.num );
				list.add(rewardObject);
				hole.getRewardMap().put(i, 1);
			}
		}

		holeDao.updateHole(hole);

		return TResult.sucess(list);
	}

	private TResult<HoleVO> newHole(long actorId, long otherActor, int holeId, int type) {
		if (otherActor >= 0) {
			Actor other = actorFacade.getActor(otherActor);
			if (other == null) {
				return TResult.valueOf(DATA_VALUE_ERROR);
			}
		}

		HoleConfig holeConfig = HoleService.getHole(holeId);
		if (holeConfig == null) {
			return TResult.valueOf(DATA_VALUE_ERROR);
		}
//		if (OpenType.ACTOR.getCode() != type || OpenType.ALLY.getCode() != type ) {
//			return Result.valueOf(DATA_VALUE_ERROR);
//		}

		Hole hole = holeDao.getHole(actorId);
		if (OpenType.ACTOR.getCode() == type) {
			if (hole != null) { // 已经开启
				return TResult.valueOf(HOLE_OPEN);
			}
			hole = Hole.valueOf(actorId, otherActor, holeConfig, type, 0);
		} else {
			int time = hole.flushTime - TimeUtils.getNow();
			hole = Hole.valueOf(actorId, otherActor, holeConfig, type, time);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("洞府开启: 玩家[%s] 开启[%s]", actorId, holeConfig.holeId));
		}

		holeDao.addHole(hole);
		
		HoleVO holeVO = null;
		if (OpenType.ACTOR.getCode() == type) {
			holeVO = HoleVO.valueOf(hole, "");
		} else {
			Actor actor = actorFacade.getActor(actorId);
			holeVO = HoleVO.valueOf(hole, actor.actorName);
		}
		return TResult.sucess(holeVO);
	}

	@Override
	public TResult<HoleVO> allyOpenHole(long actorId, long allyActor, long id) {
		Hole actorHole = holeDao.getHole(actorId);
		if (actorHole == null) {// 邀请方洞府已经关闭
			return TResult.valueOf(HOLE_CLOSE);
		}

		HoleTrigger holeTrigger = holeTriggerDao.get(allyActor);
		if (holeTrigger.allyCount >= HOLE_TRIGGER_ALLY_COUNT) {// 被邀请方的洞府触发次数不足
			return TResult.valueOf(HOLE_ALLY_COUNT_NOT_ENOUGH);
		}

		Collection<AllyVO> allys = allyfacade.getAlly(actorId);
		boolean isAlly = true;
		for (AllyVO ally : allys) {
			if (ally.actorId == allyActor) {
				isAlly = false;
			}
		}
		if (isAlly) {// 盟友不存在
			return TResult.valueOf(ALLY_NOT_EXISTS);
		}

		if (actorHole.getPkId() != id) {// 邀请方的主动触发id和消息中id不符合
			return TResult.valueOf(HOLE_CLOSE);
		}

		if (actorHole.flushTime < TimeUtils.getNow()) {// 邀请方的洞府已经关闭
			return TResult.valueOf(HOLE_CLOSE);
		}

		Hole targetHole = holeDao.getHole(actorId, allyActor, actorHole.holeId);
		if (targetHole != null) {
			return TResult.valueOf(HOLE_OPEN);
		}

		for (Long allyId : actorHole.getAllyMap().keySet()) {
			if (allyId == allyActor && actorHole.getAllyMap().get(allyId) == 0) {// 已经邀请且未通关
				ChainLock lock = LockUtils.getLock(holeTrigger);
				try {
					lock.lock();
					holeTrigger.allyCount += 1;
					holeTriggerDao.update(holeTrigger);
				} catch(Exception e) {
					LOGGER.error("{}", e);
				} finally {
					lock.unlock();
				}
				
				return newHole(actorId, allyActor, actorHole.holeId, OpenType.ALLY.getCode());
			}
		}
		return TResult.valueOf(HOLE_ALLY_NOT_INVITE);
	}

	@Override
	public Result holeFight(long id, long actorId, int battleId) {
		Hole hole = holeDao.getHole(id, actorId);
		if (hole == null) {// 没有找到洞府
			return Result.valueOf(HOLE_NOT_FIND);
		}

		HoleBattleConfig holeBattleConfig = HoleService.getHoleBattle(battleId);
		if (holeBattleConfig == null) {// 不存在这个战场
			return Result.valueOf(BATTLE_NOT_EXISTS);
		}

		Map<Integer, Integer> fightMap = hole.getFightsMap();
		for (Integer key : fightMap.keySet()) {
			if (key == battleId && fightMap.get(key) != 0) {// 这个关卡已经过关了
				return Result.valueOf(HOLE_ACCEPT_FIGHT);
			}
		}

		Map<String, Object> args = new HashMap<String, Object>();
		args.put(ID, id);
		args.put(BATTLEID, holeBattleConfig.holeBattleId);

		LineupFightModel atkLineup = LineupHelper.getLineupFight(actorId);// 玩家阵容
		args.put(HERO_MAP, atkLineup.getHeros());

		Map<Integer, Integer> monsters = holeBattleConfig.getMonsters();// 怪物配置
		Map<Integer, MonsterVO> monsterVOs = new HashMap<Integer, MonsterVO>();// 怪物阵容
		for (Integer key : monsters.keySet()) {
			monsterVOs.put(key, new MonsterVO(MonsterService.get(monsters.get(key))));
		}
		Actor actor = actorFacade.getActor(actorId);
		int actorMorale = actor.morale;// 玩家气势
		int mosterMorale = holeBattleConfig.morale;// 怪物气势
		AttackMonsterRequest event = new AttackMonsterRequest(EventKey.HOLE_BATTLE, MapService.get(holeBattleConfig.map), actorId, atkLineup,
				monsterVOs, actorMorale, mosterMorale, args, BattleType.HOLE);
		return battleFacade.submitAtkMonsterRequest(event, this);
	}

	@Override
	public Result sendAlly(long id, long actorId) {
		Collection<AllyVO> allAlly = allyfacade.getAlly(actorId);
		if (allAlly.isEmpty()) {// 没有盟友
			return Result.valueOf(NOTIFICATION_ALLY_NOT_FOUND);
		}

		Hole hole = holeDao.getHole(id, actorId);
		if (hole == null) {
			return Result.valueOf(HOLE_NOT_FIND);
		}

		// if (hole.getAllyMap().size() >= allAlly.size()) {// 已经通知过盟友
		// return Result.valueOf(NOTIFICATION_ALREADY_NOTICE);
		// }

		if (hole.type == 1) {// 是盟友邀请开启的洞府
			return Result.valueOf(HOLE_INVITE_FAIL);
		}

		for (AllyVO ally : allAlly) {
			int allyLevel = ActorHelper.getActorLevel(ally.actorId);
			if (ally.actorId == 101 || HoleRule.HOLE_OPEN_LEVEL > allyLevel) {
				continue;
			}
			Hole allyHole = holeDao.getHole(actorId, ally.actorId, hole.holeId);
			if (allyHole == null && hole.getAllyMap().containsKey(ally.actorId) == false) {
//				notifyFacade.createHole(actorId, ally.actorId, id, hole.holeId);
				HoleNotify notify = holeNotifyDao.get(ally.actorId);
				HoleNotifyVO heHoleNotifyVO = new HoleNotifyVO(actorId, id, hole.holeId, hole.flushTime);
				notify.add(heHoleNotifyVO);
				holeNotifyDao.update(notify);
				HolePushHelp.pushAllyHoleVO(ally.actorId, heHoleNotifyVO);
				hole.getAllyMap().put(ally.actorId, 0);
			}
		}
		holeDao.updateHole(hole);
		return Result.valueOf(SUCCESS);
	}

	@Override
	public Result getPackageGift(long id, long actorId) {
		Hole hole = holeDao.getHole(id, actorId);
		if (hole == null) {
			return Result.valueOf(HOLE_NOT_FIND);
		}

		Map<Long, Integer> allyMap = hole.getAllyMap();
		int count = 0;
		for (Long allyId : allyMap.keySet()) {
			if (allyMap.get(allyId) == 1) {
				count++;
			}
		}
		if (count < HOLE_ALLY_COUNT) {// 盟友通关数不够
			return Result.valueOf(HOLE_ALLY_NOT_ENOUGH);
		}

		if (hole.packageGift == 1) {// 通关大礼包已经领取
			return Result.valueOf(HOLE_REWARD_ACCEPT);
		}

		List<HoleRewardConfig> rewardList = HoleService.get(hole.holeId).getHoleGiftList();
		for (HoleRewardConfig holeRewardConfig : rewardList) {
			RewardType rewardType = RewardType.getType(holeRewardConfig.type);
			sendReward(actorId, rewardType, holeRewardConfig.id, holeRewardConfig.num);
		}
		hole.packageGift = 1;
		holeDao.updateHole(hole);
		return Result.valueOf(SUCCESS);
	}

	/**
	 * 发放奖励
	 */
	private HoleBattleResult sendReward(long actorId, RewardType rewardType, int id, int number) {
		long uuid = 0;
		HoleBattleResult holeBattleResult = new HoleBattleResult();
		switch (rewardType) {
		case GOODS:
			uuid = goodsFacade.addGoodsVO(actorId, GoodsAddType.HOLE_AWARD, id, number).item;
			holeBattleResult.awardGoods.put(uuid, number);
			break;
		case EQUIP:
			uuid = equipFacade.addEquip(actorId, EquipAddType.HOLE_AWARD, id).item;
			holeBattleResult.equipList.add(uuid);
			break;
		case HEROSOUL:
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.HOLE_REWARD, id, number);
			holeBattleResult.herosoulMap.put(id, number);
			break;
		default:
			break;
		}
		return holeBattleResult;
	}

	private int isExistHole(long actorId, int storyBattleId) {
		List<Hole> holeList = holeDao.getHoles(actorId);
		if (holeList != null) {
			for (Hole hole : holeList) {
				if (hole.type == OpenType.ACTOR.getCode()) {// 已经有了自己触发的洞府
					return 0;
				}
			}
		}
		HoleTrigger holeTrigger = holeTriggerDao.get(actorId);
		if (holeTrigger.selfCount >= HOLE_TRIGGER_SELF_COUNT) {
			return 0;
		}
		HoleProportionConfig config = HoleService.getHoleByBattleId(storyBattleId);
		int holeId = 0;
		if (config != null) {
			int level = ActorHelper.getActorLevel(actorId);
			holeId = config.getHole(level);
		}
		return holeId;
	}

//	@Override
//	public void onApplicationEvent(ContextRefreshedEvent arg0) {
//		schedule.addEverySecond(new Runnable() {
//			@Override
//			public void run() {
//				Set<Long> actors = playerSession.onlineActorList();
//				int now = TimeUtils.getNow();
//				for (Long actorId : actors) {
//					cleanHoleTrigger(actorId, now);
//					cleanHole(actorId, now);
//				}
//			}
//		}, 1);
//	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(BattleResult result) {
		FightData fightData = result.fightData;
		long actorId = result.battleReq.actorId;
		Map<String, Object> args = (Map<String, Object>) result.battleReq.args;
		long id = (long) args.get(ID);
		int battleId = (int) args.get(BATTLEID);
		Map<Integer, HeroVO> heroMap = (Map<Integer, HeroVO>) args.get(HERO_MAP);
		Hole hole = holeDao.getHole(id, actorId);
		HoleFightResponse holeFightResponse = null;
		byte battleStar = 0;
		byte holeStar = 0;
		int allyNum = 0;
		HoleBattleResult holeBattleResult = new HoleBattleResult();
		if (fightData.result.isWin()) {// 胜利
			WinLevel winLevel = fightData.result;
			battleStar = StoryHelper.computeBattleStar(fightData.result);

			// 关卡奖励
			HoleBattleConfig holeBattleConfig = HoleService.getHoleBattle(battleId);
			HoleRewardConfig reward = holeBattleConfig.getReward(fightData.result);
			if (reward != null) {
				RewardType rewardType = RewardType.getType(reward.type);
				holeBattleResult = sendReward(actorId, rewardType, reward.id, reward.num);
			}
			hole.getFightsMap().put(battleId, (int) battleStar);
			holeDao.updateHole(hole);

			// 关卡经验、声望
			// 计算声望
			long awardReputation = getReputation(actorId, holeBattleConfig, winLevel);
			holeBattleResult.awardAttribute.put(ActorAttributeKey.REPUTATION.getCode(), awardReputation);
			actorFacade.addReputation(actorId, ReputationAddType.HOLE, awardReputation);

			// 计算仙人经验
			int awardExp = holeBattleConfig.getHeroExp(winLevel);
			holeBattleResult.awardHeroExp = awardHeroExp(actorId, heroMap, result, awardExp);

			if (hole.getFightsMap().size() == HoleService.getHoleButtleNum(hole.holeId)) {// 已经通关
				if (hole.type == OpenType.ALLY.getCode()) {// 如果是盟友邀请需要更新盟友数据
					Hole allyHole = holeDao.getHole(hole.ally);
					if (allyHole != null) {
						allyHole.getAllyMap().put(actorId, 1);
						holeDao.updateHole(allyHole);
						for (Long key : allyHole.getAllyMap().keySet()) {
							if (allyHole.getAllyMap().get(key) == 1) {
								allyNum++;
							}
						}
					}
				}
				holeStar = Collections.min(hole.getFightsMap().values()).byteValue();
			}
			eventBus.post(new HoleBattleResultEvent(actorId));
		}

		if (allyNum > 0) {
			HolePushHelp.pushAllyNum(hole.ally, allyNum);// 推送通关盟友数量变更
		}
		holeBattleResult.fightData = fightData;
		holeBattleResult.battleStar = battleStar;
		holeBattleResult.holeStar = holeStar;
		holeBattleResult.holeBattleId = battleId;
		holeBattleResult.id = id;
		holeFightResponse = new HoleFightResponse(holeBattleResult);
		HolePushHelp.pushBattleResult(actorId, holeFightResponse);
	}

	/**
	 * 清理上古洞府触发数据
	 * 
	 * @param actorId
	 * @param now
	 */
	private void cleanHoleTrigger(Long actorId, int now) {
		HoleTrigger holeTrigger = holeTriggerDao.get(actorId);
		if (DateUtils.isToday(holeTrigger.operationTime) == false) {
			holeTrigger.operationTime = now;
			holeTrigger.selfCount = 0;
			holeTrigger.allyCount = 0;
			holeTriggerDao.update(holeTrigger);
		}
	}

	/**
	 * 计算奖励的声望
	 * 
	 * @param actorId
	 * @param battleConfig
	 * @param winLevel
	 * @return
	 */
	private int getReputation(long actorId, HoleBattleConfig holeBattleConfig, WinLevel winLevel) {
		int configReputatoin = holeBattleConfig.getReputation(winLevel);

		if (configReputatoin > 0) {
			Actor actor = this.actorFacade.getActor(actorId);
			int diff = actor.level - holeBattleConfig.defaultActorLevel;

			int rate = StoryRule.getRate(diff);
			configReputatoin = configReputatoin * rate / 100;
			return configReputatoin;
		}
		return 0;
	}

	@PostConstruct
	public void init() {
		eventBus.register(EventKey.STORY_PASSED, this);
	}

	@Override
	public void onEvent(Event paramEvent) {
		if (paramEvent.name == EventKey.STORY_PASSED) {
			StoryPassedEvent spe = paramEvent.convert();
			int level = ActorHelper.getActorLevel(spe.actorId);
			if(level < HoleRule.HOLE_OPEN_LEVEL){
				return;
			}
			for (int i = 0; i < spe.times; i++) {
				int holeId = isExistHole(spe.actorId, spe.battleId);
				if (holeId != 0) {// 如果没有主动触发的洞府且随机到了洞府
					HoleTrigger holeTrigger = holeTriggerDao.get(spe.actorId);
					ChainLock lock = LockUtils.getLock(holeTrigger);
					try {
						lock.lock();
						holeTrigger.selfCount += 1;
						holeTriggerDao.update(holeTrigger);
					} catch(Exception e) {
						LOGGER.error("{}", e);
					} finally {
						lock.unlock();
					}
					TResult<HoleVO> result = newHole(spe.actorId, -1, holeId, 0);
					if (result.isOk()) {
						HolePushHelp.pushHoleOpen(spe.actorId, result.item);
					}
				}
			}
		}
	}

	private Map<Integer, Integer> awardHeroExp(long actorId, Map<Integer, HeroVO> heroMap, BattleResult result, Integer awardExp) {
		Map<Integer, List<String>> map = null;
		if(result != null){
			map = result.addExpExpr.get(actorId);
		}
		Map<Integer, Integer> resultMap  = new HashMap<>();
		for (HeroVO hero : heroMap.values()) {
			Integer totalAwardExp = awardExp;
			if (map != null) {
				List<String> addExpExprList = map.get(hero.getHeroId());
				if (addExpExprList != null) {
					for (String expr : addExpExprList) {
						totalAwardExp += FormulaHelper.executeCeilInt(expr, awardExp);
					}
				}
			}
			
			this.heroFacade.addHeroExp(actorId, hero.heroId, totalAwardExp);
			resultMap.put(hero.getHeroId(), totalAwardExp);
		}
		
		return resultMap;
	}
	
	@Override
	public void onZero() {
		Set<Long> actors = playerSession.onlineActorList();
		for (Long actorId : actors) {
			HoleTrigger holeTrigger = holeTriggerDao.get(actorId);
			ChainLock lock = LockUtils.getLock(holeTrigger);
			try {
				lock.lock();
				holeTrigger.selfCount = 0;
				holeTrigger.allyCount = 0;
				holeTriggerDao.update(holeTrigger);
				HolePushHelp.pushZero(actorId);
			} catch(Exception e) {
				LOGGER.error("{}", e);
			} finally {
				lock.unlock();
			}
			
		}
	}
	
	@Override
	public void onLogin(long actorId) {
		cleanHoleTrigger(actorId, TimeUtils.getNow());
	}
}
