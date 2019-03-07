package com.jtang.gameserver.module.demon.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.ACTOR_ID_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.DEMON_CAMP_HAD_JION;
import static com.jiatang.common.GameStatusCodeConstant.DEMON_CAMP_NOT_JOIN;
import static com.jiatang.common.GameStatusCodeConstant.DEMON_IN_DIFFRENT;
import static com.jiatang.common.GameStatusCodeConstant.DEMON_IN_SAME_CAMP;
import static com.jiatang.common.GameStatusCodeConstant.DEMON_LAST_NOT_JION;
import static com.jiatang.common.GameStatusCodeConstant.DEMON_MONSTER_DEAD;
import static com.jiatang.common.GameStatusCodeConstant.DEMON_NOT_ATTACK;
import static com.jiatang.common.GameStatusCodeConstant.DEMON_NOT_JION;
import static com.jiatang.common.GameStatusCodeConstant.DEMON_NOT_OPEN;
import static com.jiatang.common.GameStatusCodeConstant.DEMON_SOCRE_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_EXISTS;
import static com.jtang.core.protocol.StatusCode.TICKET_NOT_ENOUGH;
import static com.jtang.gameserver.module.demon.constant.DemonRule.DEMON_MAP_ID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.DemonCampConfig;
import com.jtang.gameserver.dataconfig.model.DemonConfig;
import com.jtang.gameserver.dataconfig.model.DemonEndRewardConfig;
import com.jtang.gameserver.dataconfig.model.DemonExchangeConfig;
import com.jtang.gameserver.dataconfig.model.DemonMonsterConfig;
import com.jtang.gameserver.dataconfig.model.DemonScoreConfig;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.dataconfig.service.DemonCampService;
import com.jtang.gameserver.dataconfig.service.DemonEndRewardService;
import com.jtang.gameserver.dataconfig.service.DemonExchangeService;
import com.jtang.gameserver.dataconfig.service.DemonGlobalService;
import com.jtang.gameserver.dataconfig.service.DemonMonsterService;
import com.jtang.gameserver.dataconfig.service.DemonScoreService;
import com.jtang.gameserver.dataconfig.service.DemonService;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dataconfig.service.MonsterService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Demon;
import com.jtang.gameserver.dbproxy.entity.DemonGlobal;
import com.jtang.gameserver.dbproxy.entity.DemonRank;
import com.jtang.gameserver.dbproxy.entity.Power;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackMonsterRequest;
import com.jtang.gameserver.module.battle.model.AttackPlayerRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.chat.facade.ChatFacade;
import com.jtang.gameserver.module.demon.dao.DemonDao;
import com.jtang.gameserver.module.demon.dao.DemonGlobalDao;
import com.jtang.gameserver.module.demon.dao.DemonRankDao;
import com.jtang.gameserver.module.demon.facade.DemonFacade;
import com.jtang.gameserver.module.demon.handler.response.BossAttChangeResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonBossAttackResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonCampDataResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonEndRewardResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonExchangeResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonLastRankResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonLastRewardResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonPlayerAttackResponse;
import com.jtang.gameserver.module.demon.handler.response.FeatsChangeResponse;
import com.jtang.gameserver.module.demon.helper.DemonHelper;
import com.jtang.gameserver.module.demon.helper.DemonPushHelper;
import com.jtang.gameserver.module.demon.model.DemonDifficultData;
import com.jtang.gameserver.module.demon.model.DemonModel;
import com.jtang.gameserver.module.demon.model.DemonRankVO;
import com.jtang.gameserver.module.demon.model.DemonVO;
import com.jtang.gameserver.module.demon.model.OpenTime;
import com.jtang.gameserver.module.demon.type.DemonCamp;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.monster.facade.DemonMonsterFacade;
import com.jtang.gameserver.module.notice.facade.NoticeFacade;
import com.jtang.gameserver.module.power.facade.PowerFacade;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.sysmail.type.SysmailType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 集众降魔实现
 * 
 * @author ludd
 * 
 */
@Component
public class DemonFacadeImpl implements DemonFacade, ApplicationListener<ContextRefreshedEvent>, BattleCallBack {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemonFacadeImpl.class);

	@Autowired
	private Schedule schedule;
	@Autowired
	private PowerFacade powerFacade;

	@Autowired
	private ActorFacade actorFacade;

	@Autowired
	private LineupFacade lineupFacade;

	@Autowired
	private BattleFacade battleFacade;

	@Autowired
	private VipFacade vipFacade;

	@Autowired
	private DemonDao demonDao;

	@Autowired
	private ChatFacade chatFacade;

	@Autowired
	private SysmailFacade sysmailFacade;

	@Autowired
	private NoticeFacade noticeFacade;

	@Autowired
	private DemonMonsterFacade demonMonsterFacade;

	@Autowired
	private DemonGlobalDao demonGlobalDao;

	@Autowired
	private DemonRankDao demonRankDao;

	@Autowired
	private PlayerSession playerSession;

	@Autowired
	private EquipFacade equipFacade;

	@Autowired
	private GoodsFacade goodsFacade;

	@Autowired
	private HeroSoulFacade heroSoulFacade;

	@Autowired
	private IconFacade iconFacade;

	/**
	 * key:难度
	 */
	private Map<Integer, DemonDifficultData> demonDifficultDatas = new ConcurrentHashMap<Integer, DemonDifficultData>();
	/**
	 * key:actorId
	 */
	private Map<Long, DemonModel> demons = new ConcurrentHashMap<>();

	private Map<Integer, DemonCampConfig> currentCampConfig = new HashMap<>();

	/**
	 * 是否开放
	 */
	private boolean isOpen = false;

	/**
	 * 正在使用的时间
	 */
	private OpenTime openTime;

	/**
	 * 已读奖励标识 key：角色id value 1:已读 0：未读
	 */
	private Map<Long, Byte> readRewardFlag = new ConcurrentHashMap<>();

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(new Runnable() {

			@Override
			public void run() {
				if (DemonGlobalService.inOpenDate() == false) {
					return;
				}
				if (isOpen == false && openTime == null) { // 未开放时才可开放
					List<OpenTime> list = DemonGlobalService.getOpenTimes();
					for (OpenTime time : list) {// 取一个开放时间
						if (time.isStart()) {
							start();
							openTime = time;
							isOpen = true;
							break;
						}
					}
				}

				if (isOpen == true && openTime != null) {// 开放时才可关闭
					if (openTime.isStart() == false) {
						openTime = null;
						isOpen = false;
						end();

					}
				}
			}
		}, 2);

	}

	/**
	 * 集众降魔开始
	 */
	private void start() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("集众降魔开始");
		}
		int rankMax = DemonService.getPowerRankMax();
		List<Long> list = powerFacade.getRankList(rankMax);
		GameOssLogger.demonJoinList(list);
		// 分组角色
		for (Long actorId : list) {
			if (actorId == 0) {
				continue;
			}
			Actor actor = actorFacade.getActor(actorId);
			if (actor.level < DemonGlobalService.getActorLevel()) {
				continue;
			}
			Power power = powerFacade.getPower(actorId);
			DemonConfig cfg = DemonService.getByPowerRank(power.rank);
			if (cfg == null) {
				continue;
			}
			int dif = cfg.getDifficultId();
			DemonDifficultData groupData = null;
			if (demonDifficultDatas.containsKey(dif)) {
				groupData = demonDifficultDatas.get(dif);
			} else {
				groupData = new DemonDifficultData(dif, Collections.synchronizedList(new ArrayList<DemonModel>()));
				demonDifficultDatas.put(dif, groupData);
			}
			List<DemonModel> set = groupData.getDemons();
			Demon demon = demonDao.get(actorId);
			DemonModel dm = new DemonModel(demon, dif);
			set.add(dm);
			demons.put(dm.getActorId(), dm);
		}
		// 创建boss
		for (Map.Entry<Integer, DemonDifficultData> entry : demonDifficultDatas.entrySet()) {
			DemonDifficultData groupData = entry.getValue();
			createBoss(groupData);
			DemonCampConfig campConfig = DemonCampService.getRandomConfig(groupData.getDifficult());
			if (campConfig != null) {
				currentCampConfig.put(campConfig.getDifficult(), campConfig);
			}
		}
	}

	/**
	 * 集众降魔结束
	 */
	private void end() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("集众降魔结束");
		}
		if (demons.isEmpty() || demonDifficultDatas.isEmpty()) {
			return;
		}
		List<DemonRank> allDemonRank = new ArrayList<>();// 所有难度排行实体集合
		for (Map.Entry<Integer, DemonDifficultData> entry : demonDifficultDatas.entrySet()) { // N个难度
			int diff = entry.getKey();
			DemonDifficultData gd = entry.getValue();
			DemonConfig cfg = DemonService.get(diff);
			if (cfg == null) {
				continue;
			}
			Map<Long, Integer> sortActorIdMap = gd.sortFeats();
			List<DemonModel> win = gd.winCamp();
			for (DemonModel demonModel : gd.getDemons()) { // 单个难度玩家处理
				if (demonModel.getCamp() == 0) {// 无阵营 不处理
					continue;
				}
				long actorId = demonModel.getActorId();
				int rank = sortActorIdMap.get(actorId);
				if (rank == 0) {// 无排名 不处理
					continue;
				}

				DemonRank demonRank = createNewDemonRank(demonModel, diff, rank, cfg, win, gd);
				allDemonRank.add(demonRank);
			}

			// 记录怪物伤害
			int hurt = gd.getMonsterHurt();
			DemonGlobal demonGlobal = demonGlobalDao.get(gd.getDifficult());
			demonGlobal.lastHeart = hurt;
			if (hurt == 0) { // 没打死 时间为0
				demonGlobal.bossDeadUseTime = 0;
			}
			demonGlobalDao.update(demonGlobal);
		}
		demonRankDao.update(allDemonRank);

		// 给所有参与者发通知,推排行，推奖励面板
		DemonDifficultData hardDifficultData = demonDifficultDatas.get(DemonService.getHardDiffcult());
		DemonModel hardModel = null;
		if (hardDifficultData != null) {
			hardModel = hardDifficultData.getWinFristFeats();
		}
		readRewardFlag.clear();
		for (DemonRank demonRank : allDemonRank) {
			// notifyFacade.createDemonReward(demonRank.getPkId(),
			// demonRank.getFirstDemonReward(), demonRank.getFeatsRankReward(),
			// demonRank.getWinCampReward(), demonRank.getUseTicketReward(),
			// demonRank.lastRewardScore, demonRank.lastIsWin,
			// demonRank.lastRank);
			List<RewardObject> allReward = new ArrayList<>();
			allReward.addAll(demonRank.getFirstDemonReward());
			allReward.addAll(demonRank.getFeatsRankReward());
			allReward.addAll(demonRank.getFeatsTotalReward());
			allReward.addAll(demonRank.getUseTicketReward());
			sysmailFacade.sendSysmail(demonRank.getPkId(), SysmailType.DEMON, allReward, demonRank.lastRewardScore, demonRank.lastIsWin, demonRank.lastRank);
			if (hardModel != null && demonRank.getPkId() == hardModel.getActorId()) {
				chatFacade.sendDemonWinChat(hardModel.getActorId(), demonRank.getFirstDemonReward(), demonRank.getFeatsTotalReward());
			}
			readRewardFlag.put(demonRank.getPkId(), (byte) 0);
			DemonEndRewardResponse demonEndRewardResponse = new DemonEndRewardResponse(demonRank.getFirstDemonReward(), demonRank.getFeatsRankReward(), demonRank.getFeatsTotalReward(),
					demonRank.getUseTicketReward(), demonRank.lastRewardScore, demonRank.lastIsWin, demonRank.lastRank);

			DemonPushHelper.pushDemonReward(demonRank.getPkId(), demonEndRewardResponse);
		}

		// 数据清理
		demonDifficultDatas.clear();
		demons.clear();
		currentCampConfig.clear();

	}

	@Override
	public TResult<DemonCampDataResponse> getDemonData(long actorId) {
		if (isStart() == false) {
			return TResult.valueOf(DEMON_NOT_OPEN);
		}
		if (demons.containsKey(actorId) == false) {
			return TResult.valueOf(DEMON_NOT_JION);
		}
		DemonModel actorDemon = demons.get(actorId);
		int dif = actorDemon.difficult;

		List<DemonVO> list = new ArrayList<>();
		DemonDifficultData groupData = demonDifficultDatas.get(dif);
		for (DemonModel demon : groupData.getDemons()) {
			DemonVO demonVO = demon2demonVO(demon);
			if (demon.getCamp() != 0 && demon.getCamp() == DemonCamp.BLUE_CAMP.getCode()) {
				list.add(demonVO);
			} else if (demon.getCamp() != 0 && demon.getCamp() == DemonCamp.RED_CAMP.getCode()) {
				list.add(demonVO);
			}
		}
		int bossHp = 0;
		int bossHpMax = 0;
		int bossId = 0;
		for (MonsterVO monsterVO : groupData.getBoss()) {
			bossHp = monsterVO.getHp();
			bossHpMax = monsterVO.getMaxHp();
			bossId = monsterVO.getHeroId();
			break;
		}
		int time = getAttackBossTime(actorId);
		DemonCampDataResponse demonCampDataResponse = new DemonCampDataResponse(list, bossHp, bossHpMax, dif, bossId, actorDemon.attackNum, time);
		return TResult.sucess(demonCampDataResponse);
	}

	@Override
	public TResult<DemonCampDataResponse> joinCamp(long actorId) {
		if (isStart() == false) {
			return TResult.valueOf(DEMON_NOT_OPEN);
		}
		if (demons.containsKey(actorId) == false) {
			return TResult.valueOf(DEMON_NOT_JION);
		}
		DemonModel demon = demons.get(actorId);
		if (demon.getCamp() != 0) {
			return TResult.valueOf(DEMON_CAMP_HAD_JION);
		}
		Power power = powerFacade.getPower(actorId);
		int powerRank = 0;
		if (power != null) {
			powerRank = power.rank;
		}
		DemonCampConfig campConfig = currentCampConfig.get(demon.difficult);
		DemonCamp demonCamp = DemonCamp.ALL_CAMP;
		if (campConfig != null) {
			if (campConfig.getRedCampSet().contains(powerRank)) {
				demonCamp = DemonCamp.RED_CAMP;
			} else if (campConfig.getBlueCampSet().contains(powerRank)) {
				demonCamp = DemonCamp.BLUE_CAMP;
			}
		}
		DemonDifficultData groupData = demonDifficultDatas.get(demon.difficult);
		if (demonCamp == null || demonCamp.equals(DemonCamp.ALL_CAMP)) {
			int redCampSize = groupData.getCampSize(DemonCamp.RED_CAMP.getCode());
			int blueCampSize = groupData.getCampSize(DemonCamp.BLUE_CAMP.getCode());
			demonCamp = redCampSize >= blueCampSize ? DemonCamp.BLUE_CAMP : DemonCamp.RED_CAMP;
		}
		if (groupData.isFirstJoin()) {
			groupData.setFirstCamp(demonCamp);
		}
		demon.setCamp(demonCamp.getCode());
		broadcastJoin(groupData, demon);

		return getDemonData(actorId);
	}

	@Override
	public Result attackPlayer(long actorId, long targetId, boolean useTicket) {
		if (isStart() == false) {
			return Result.valueOf(DEMON_NOT_OPEN);
		}
		if (demons.containsKey(actorId) == false || demons.containsKey(targetId) == false) {
			return Result.valueOf(DEMON_NOT_JION);
		}
		DemonModel demon = demons.get(actorId);
		DemonModel targetDemon = demons.get(targetId);
		if (demon.difficult != targetDemon.difficult) {
			return Result.valueOf(DEMON_IN_DIFFRENT);
		}
		if (demon.getCamp() == targetDemon.getCamp()) {
			return Result.valueOf(DEMON_IN_SAME_CAMP);
		}
		if (demon.getCamp() == 0 || targetDemon.getCamp() == 0) {
			return Result.valueOf(DEMON_CAMP_NOT_JOIN);
		}
		DemonConfig cfg = DemonService.get(demon.difficult);
		if (cfg == null) {
			return Result.valueOf(DEMON_NOT_ATTACK);
		}

		if (useTicket) {
			if (demon.attackNum >= cfg.getAttackPlayerNum()) {// 扣点券
				int ticket = vipFacade.getTicket(demon.getActorId());
				int useTicketNum = cfg.getAtkPlayerUseTicketNum();
				if (ticket < useTicketNum) {
					return Result.valueOf(TICKET_NOT_ENOUGH);
				}
			}
		} else {
			if (demon.attackNum >= cfg.getAttackPlayerNum()) {
				return Result.valueOf(DEMON_NOT_ATTACK);
			}
		}

		if (cfg.getAttackPlayerNum() > demon.attackNum) {
			demon.attackNum += 1;
		}

		// 构建战斗
		LineupFightModel atkLineup = LineupHelper.getLineupFight(actorId);
		LineupFightModel defLineup = LineupHelper.getLineupFight(targetId);
		Actor actor = actorFacade.getActor(actorId);
		Actor targetActor = actorFacade.getActor(targetId);
		if (targetActor == null) {
			return Result.valueOf(ACTOR_ID_ERROR);
		}
		MapConfig fightMap = MapService.get(DEMON_MAP_ID);
		AttackPlayerRequest attackReq = new AttackPlayerRequest(EventKey.LEARN_BATTLE, fightMap, actorId, atkLineup, targetId, defLineup, actor.morale, targetActor.morale, useTicket,
				BattleType.DEMON_ATTACK_PLAYER);
		Result result = battleFacade.submitAtkPlayerRequest(attackReq, this);
		if (result.isFail()) {// 战斗没有开打,前期检查没有通过
			return Result.valueOf(result.statusCode);
		}

		return result;
	}

	@Override
	public void execute(BattleResult result) {
		BattleType battleType = result.battleReq.battleType;
		if (battleType.equals(BattleType.DEMON_ATTACK_BOSS)) {
			boolean useTicket = (boolean) result.battleReq.args;
			handlerAttackBossResult(result, useTicket);
		} else if (battleType.equals(BattleType.DEMON_ATTACK_PLAYER)) {
			boolean useTicket = (boolean) result.battleReq.args;
			handlerAttackPlayerResult(result, useTicket);
		}
	}

	/**
	 * 处理攻击玩家结果
	 * 
	 * @param result
	 */
	private void handlerAttackPlayerResult(BattleResult result, boolean useTicket) {
		AttackPlayerRequest attackReq = (AttackPlayerRequest) result.battleReq;
		long actorId = attackReq.actorId;
		if (isStart() == false) {
			DemonPushHelper.attackPlayerFail(actorId, Result.valueOf(DEMON_NOT_OPEN));
			return;
		}

		long targetId = attackReq.targetActorId;
		DemonModel demon = demons.get(actorId);
		if (useTicket) {
			DemonDifficultData groupData = demonDifficultDatas.get(demon.difficult);
			DemonConfig cfg = DemonService.get(groupData.getDifficult());
			int ticket = vipFacade.getTicket(actorId);
			int useTicketNum = cfg.getAtkBossUseTicketNum();
			if (ticket < useTicketNum) {
				DemonPushHelper.attackPlayerFail(actorId, Result.valueOf(TICKET_NOT_ENOUGH));
				return;
			}
			vipFacade.decreaseTicket(actorId, TicketDecreaseType.DEMON_ATTACK_PLAYER, useTicketNum, 0, 0);
			DemonDifficultData demonDifficultData = demonDifficultDatas.get(demon.difficult);
			demonDifficultData.recordUseTicket(actorId, useTicketNum);
		}

		DemonModel target = demons.get(targetId);
		long addValue = 0;
		int extFeats = 0;
		;

		if (result.fightData.result.isWin()) {
			DemonDifficultData groupData = demonDifficultDatas.get(demon.difficult);
			DemonConfig cfg = DemonService.get(groupData.getDifficult());
			addValue = cfg.getFeatsOfPlayer(target.feats);
			addValue = addValue > cfg.getMaxFeatsOfPlayer() ? cfg.getMaxFeatsOfPlayer() : addValue; // 功勋值有上限
			addValue = addValue > 0 ? addValue : 1;// 保底
			demon.feats += addValue;
			// 额外功勋值计算
			int playerLessNum = groupData.getCampDiffrentSize(demon.getCamp());
			extFeats = cfg.getExtraFeats(playerLessNum, addValue);
			demon.feats += extFeats;

			noticeFacade.broadcastDemonSnatch(targetId, actorId);
			FeatsChangeResponse featsChangeResponse = new FeatsChangeResponse();
			featsChangeResponse.addValue(demon.getActorId(), demon.feats);
			for (DemonModel d : groupData.getDemons()) {
				DemonPushHelper.pushFeatsChange(d.getActorId(), featsChangeResponse);
			}
		}

		DemonPlayerAttackResponse demonPlayerAttackResponse = new DemonPlayerAttackResponse(result.fightData, addValue, demon.attackNum, extFeats);
		DemonPushHelper.pushAttackPlayerResult(actorId, demonPlayerAttackResponse);
	}

	/**
	 * 处理攻击boss结果
	 * 
	 * @param result
	 */
	private void handlerAttackBossResult(BattleResult result, boolean useTicket) {
		AttackMonsterRequest attackMonsterRequest = (AttackMonsterRequest) result.battleReq;
		long actorId = attackMonsterRequest.actorId;
		if (isStart() == false) {
			DemonPushHelper.attackBossFail(actorId, Result.valueOf(DEMON_NOT_OPEN));
			return;
		}

		DemonModel demonModel = demons.get(actorId);
		DemonDifficultData groupData = demonDifficultDatas.get(demonModel.difficult);
		ChainLock lock = LockUtils.getLock(groupData);
		try {
			lock.lock();
			boolean bossDead = groupData.monsterAllDead();
			if (bossDead) { // 打完发现boss已死
				DemonPushHelper.attackBossFail(actorId, Result.valueOf(DEMON_MONSTER_DEAD));
				return;
			}

			DemonConfig cfg = DemonService.get(groupData.getDifficult());
			if (useTicket) {
				int ticket = vipFacade.getTicket(demonModel.getActorId());
				int useTicketNum = cfg.getAtkBossUseTicketNum();
				if (ticket < useTicketNum) {
					DemonPushHelper.attackBossFail(actorId, Result.valueOf(TICKET_NOT_ENOUGH));
					return;
				}
				vipFacade.decreaseTicket(actorId, TicketDecreaseType.DEMON_ATTACK_BOSS, useTicketNum, 0, 0);
				DemonDifficultData demonDifficultData = demonDifficultDatas.get(demonModel.difficult);
				demonDifficultData.recordUseTicket(actorId, useTicketNum);
			}

			List<Fighter> fighters = result.defendsTeam;
			Map<AttackerAttributeKey, Integer> attributes = new HashMap<>();
			int monsterId = 0;

			Collection<MonsterVO> boss = groupData.getBoss();
			for (Fighter fighter : fighters) {
				for (MonsterVO monsterVO : boss) {
					if (fighter.getHeroId() == monsterVO.getHeroId()) {
						int hp = monsterVO.getHp() - fighter.getHert();
						hp = hp < 0 ? 0 : hp;
						monsterVO.setHp(hp);
						attributes.put(AttackerAttributeKey.HP, monsterVO.getHp());
						monsterId = monsterVO.getHeroId();
						continue;
					}
				}
			}
			bossDead = groupData.monsterAllDead();
			

			if (bossDead == false) {
				if (result.fightData.result.isWin()) {
					result.fightData.result = WinLevel.FAIL;
				}
			}

			List<RewardObject> reward = new ArrayList<>();
			Actor actor = actorFacade.getActor(demonModel.getActorId());
			if (bossDead) {
				DemonGlobal demonGlobal = demonGlobalDao.get(groupData.getDifficult());
				int bossDeadUseTime = TimeUtils.getNow() - openTime.getStartSeconds();
				demonGlobal.bossDeadUseTime = bossDeadUseTime;
				reward = cfg.getKillBossRewardObjects(actor.level);
				int vipLevel = vipFacade.getVipLevel(demonModel.getActorId());
				String boosName = MonsterService.get(monsterId).getMonsterName();
				chatFacade.sendDemonBossChat(actor.actorName, actor.getPkId(), actor.level, vipLevel, boosName, reward);
				sendReward(demonModel.getActorId(), reward);
				
				List<DemonModel> killCamp = groupData.getDemonModels(demonModel.getCamp());
				for (DemonModel temp : killCamp) {
					if (temp.getActorId() == demonModel.getActorId()) {
						continue;
					}
					Actor act = actorFacade.getActor(temp.getActorId());
					sendReward(temp.getActorId(), cfg.getKillBossCampRewardObjects(act.level));
				}
				LOGGER.info(String.format("集众降魔boss被击杀, difficult:[%s], actorId:[%s]", groupData.getDifficult(), actor.getPkId()));
				GameOssLogger.demonKillBoss(Game.getServerId(), actor.getPkId(), groupData.getDifficult());
			} else {
				reward = cfg.getFailRewardObjects(actor.level);
				sendReward(demonModel.getActorId(), reward);
			}

			int extScore = attackBossRewardScore(demonModel, cfg);

			long addFeats = ensureValidFeats(cfg.getFeatsOfBoss(), result.fightData.atkTeamHitPoints);
			demonModel.feats += addFeats;
			// 额外功勋值计算
			int playerLessNum = groupData.getCampDiffrentSize(demonModel.getCamp());
			int extFeats = cfg.getExtraFeats(playerLessNum, addFeats);
			demonModel.feats += extFeats;

			FeatsChangeResponse featsChangeResponse = new FeatsChangeResponse();
			featsChangeResponse.addValue(demonModel.getActorId(), demonModel.feats);

			BossAttChangeResponse bossAttChangeResponse = new BossAttChangeResponse(demonModel.getActorId(), attributes);
			for (DemonModel d : groupData.getDemons()) {
				DemonPushHelper.pushFeatsChange(d.getActorId(), featsChangeResponse);
				DemonPushHelper.pushBossAttChange(d.getActorId(), bossAttChangeResponse);
			}

			DemonBossAttackResponse demonBossAttackResponse = new DemonBossAttackResponse(result.fightData, reward, addFeats, getAttackBossTime(actorId), extFeats, extScore);
			DemonPushHelper.pushAttackBossResult(actorId, demonBossAttackResponse);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 创建boss
	 * 
	 * @param groupData
	 */
	private void createBoss(DemonDifficultData groupData) {
		int totalLevel = 0;
		for (DemonModel d : groupData.getDemons()) {
			Actor actor = actorFacade.getActor(d.getActorId());
			if (actor == null) {
				continue;
			}
			totalLevel += actor.level;
		}
		DemonConfig cfg = DemonService.get(groupData.getDifficult());
		Map<Integer, MonsterVO> m = demonMonsterFacade.getMonster(cfg.getDemonMonsterId(), totalLevel);
		DemonGlobal dm = demonGlobalDao.get(groupData.getDifficult());
		DemonMonsterConfig dmCfg = DemonMonsterService.get(cfg.getDemonMonsterId());
		int extraHp = dmCfg.getExtraHp(dm.lastHeart, dm.bossDeadUseTime);
		for (MonsterVO monsterVO : m.values()) {
			if (extraHp > 0) {// 如果上次伤害为0，已基础值算，否则以上次伤害算
				monsterVO.setHp(extraHp);// 设置附加血
				monsterVO.setMaxHp(extraHp);
			}
		}
		groupData.setMonsterVO(m);
		DemonMonsterConfig mCfg = DemonMonsterService.get(cfg.getDemonMonsterId());
		groupData.setMonsterMorale(mCfg.getMorale());
	}

	@Override
	public Result attackBoss(long actorId, boolean useTicket) {
		if (isStart() == false) {
			return Result.valueOf(DEMON_NOT_OPEN);
		}
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_ID_ERROR);
		}

		if (demons.containsKey(actorId) == false) {
			return Result.valueOf(DEMON_NOT_JION);
		}
		DemonModel demon = demons.get(actorId);
		if (demon.getCamp() == 0) {
			return Result.valueOf(DEMON_CAMP_NOT_JOIN);
		}
		DemonDifficultData groupData = demonDifficultDatas.get(demon.difficult);
		if (groupData.monsterAllDead()) {
			return Result.valueOf(DEMON_MONSTER_DEAD);
		}

		DemonConfig cfg = DemonService.get(groupData.getDifficult());
		if (cfg == null) {
			return Result.valueOf(DEMON_NOT_JION);
		}
		
		int interval = TimeUtils.getNow() - demon.attackBossTime;
		if (interval < cfg.getInterval()) {
			if (useTicket) {
				int ticket = vipFacade.getTicket(demon.getActorId());
				if (ticket < cfg.getAtkBossUseTicketNum()) {
					return Result.valueOf(TICKET_NOT_ENOUGH);
				}
			} else {
				return Result.valueOf(DEMON_NOT_ATTACK);
			}
		} else {
			useTicket = false;
		}
//		if (useTicket) {
//			if (interval < cfg.getInterval()) {
//				int ticket = vipFacade.getTicket(demon.getActorId());
//				if (ticket < cfg.getAtkBossUseTicketNum()) {
//					return Result.valueOf(TICKET_NOT_ENOUGH);
//				}
//				// 战斗计算完成后扣除点券
//			}
//		} else {
//			if (interval < cfg.getInterval()) {
//				return Result.valueOf(DEMON_NOT_ATTACK);
//			}
//		}
		LineupFightModel atkLineup = LineupHelper.getLineupFight(actorId);
		Map<Integer, MonsterVO> defTeam = groupData.getMonsterVO(); // 拿boss
		MapConfig fightMap = MapService.get(DEMON_MAP_ID);
		AttackMonsterRequest attackMonsterRequest = new AttackMonsterRequest(EventKey.LEARN_BATTLE, fightMap, actorId, atkLineup, defTeam, actor.morale, groupData.getMonsterMorale(), useTicket,
				BattleType.DEMON_ATTACK_BOSS);
		attackMonsterRequest.skipFirstRound = true;
		Result result = battleFacade.submitAtkMonsterRequest(attackMonsterRequest, this);
		if (result.isOk()) {
			demon.attackBossTime = TimeUtils.getNow();
		}
		return result;
	}

	/**
	 * 广播加入阵营
	 * 
	 * @param groupData
	 * @param demon
	 */
	private void broadcastJoin(DemonDifficultData groupData, DemonModel demon) {
		List<DemonModel> list = groupData.getDemons();
		for (DemonModel d : list) {
			if (d.getActorId() != demon.getActorId()) {
				DemonPushHelper.pushJoinPlayer(d.getActorId(), demon2demonVO(demon));
			}
		}
	}

	@Override
	public TResult<DemonExchangeResponse> exchangeReward(long actorId, int id, int exchangeNum) {
		// if (DemonGlobalService.enableExchange() == false) {
		// return TResult.valueOf(DEMON_NOT_EXCHANGE);
		// }
		DemonExchangeConfig cfg = DemonExchangeService.get(id);
		if (cfg == null) {
			return TResult.valueOf(GOODS_NOT_EXISTS);
		}

		int num = cfg.getNum() * exchangeNum;
		int useScore = cfg.getConsumeScore() * exchangeNum;

		Demon demon = demonDao.get(actorId);
		if (demon.score < useScore) {
			return TResult.valueOf(DEMON_SOCRE_NOT_ENOUGH);
		}

		demon.score -= useScore;
		demonDao.update(demon);
		Actor actor = actorFacade.getActor(demon.getPkId());
		RewardObject rewardObject = new RewardObject(RewardType.getType(cfg.getType()), cfg.getGoodsId(), num);

		GameOssLogger.demonScoreDecrease(actor.uid, actor.platformType, actor.channelId, Game.getServerId(), actorId, useScore, demon.score, rewardObject);
		sendReward(actorId, cfg.getGoodsId(), num, rewardObject.rewardType);
		DemonExchangeResponse demonExchangeResponse = new DemonExchangeResponse(demon.score, rewardObject);

		return TResult.sucess(demonExchangeResponse);
	}

	@Override
	public boolean isDemon(long actorId) {
		if (isStart()) {
			if (demons.containsKey(actorId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public long getDemonScore(long actorId) {
		Demon demon = demonDao.get(actorId);
		return demon.score;
	}

	@Override
	public TResult<DemonLastRankResponse> getLastRankData(long actorId) {
		DemonRank demonRank = demonRankDao.get(actorId);
		long difficult = 0;
		DemonRankVO selfDemonRankVO = null;
		Actor self = actorFacade.getActor(actorId);
		IconVO selfIcon = iconFacade.getIconVO(actorId);
		if (demonRank == null) {
			difficult = DemonGlobalService.getDefaultRankDiffcult();
			selfDemonRankVO = new DemonRankVO(self.getPkId(), self.actorName, selfIcon);
		} else {
			difficult = demonRank.lastDifficult;
			selfDemonRankVO = new DemonRankVO(self.getPkId(), demonRank.lastRank, self.actorName, demonRank.lastFeats, demonRank.lastIsWin, selfIcon);
		}

		int rankNum = DemonGlobalService.getRankNum();

		List<DemonRank> ranks = demonRankDao.getByCondition(difficult, rankNum);
		List<DemonRankVO> results = new ArrayList<>();
		for (DemonRank rank : ranks) {
			Actor actor = actorFacade.getActor(rank.getPkId());
			IconVO iconVO = iconFacade.getIconVO(rank.getPkId());
			DemonRankVO demonRankVO = new DemonRankVO(rank.getPkId(), rank.lastRank, actor.actorName, rank.lastFeats, rank.lastIsWin, iconVO);
			results.add(demonRankVO);
		}
		DemonLastRankResponse demonLastRankResponse = new DemonLastRankResponse(selfDemonRankVO, results);
		return TResult.sucess(demonLastRankResponse);
	}

	@Override
	public TResult<DemonLastRewardResponse> getLastRewardData(long actorId) {
		DemonRank demonRank = demonRankDao.get(actorId);
		if (demonRank == null) {
			return TResult.sucess(new DemonLastRewardResponse());
		}

		if (readRewardFlag.containsKey(actorId) == false) {
			return TResult.sucess(new DemonLastRewardResponse());
		}

		byte isRead = readRewardFlag.get(actorId);
		DemonEndRewardResponse demonEndRewardResponse = new DemonEndRewardResponse(demonRank.getFirstDemonReward(), demonRank.getFeatsRankReward(), demonRank.getFeatsTotalReward(),
				demonRank.getUseTicketReward(), demonRank.lastRewardScore, demonRank.lastIsWin, demonRank.lastRank);
		DemonLastRewardResponse demonLastRewardResponse = new DemonLastRewardResponse(isRead, demonEndRewardResponse);
		return TResult.sucess(demonLastRewardResponse);
	}

	@Override
	public Result setRewardRead(long actorId) {
		if (readRewardFlag.containsKey(actorId) == false) {
			return Result.valueOf(DEMON_LAST_NOT_JION);
		}
		readRewardFlag.put(actorId, (byte) 1); // 设置已读
		return Result.valueOf();
	}

	/**
	 * 创建一个新排名实体
	 * 
	 * @param demonModel
	 *            集众降魔数据
	 * @param diff
	 *            难度
	 * @param rank
	 *            排名
	 * @param cfg
	 *            集众降魔配置
	 * @param win
	 *            胜利列表
	 * @param gd
	 * @return
	 */
	private DemonRank createNewDemonRank(DemonModel demonModel, int diff, int rank, DemonConfig cfg, List<DemonModel> win, DemonDifficultData gd) {
		Demon demon = demonModel.getDemon();
		long actorId = demonModel.getActorId();
		byte isWin = (byte) (win.contains(demonModel) ? 1 : 0);
		DemonEndRewardConfig endCfg = DemonEndRewardService.get(diff, rank);
		List<RewardObject> featsRankReward = new ArrayList<>();
		if (endCfg != null) {
			featsRankReward = endCfg.getRewardObjects();
		}

		// 第一名奖励
		List<RewardObject> firstReward = new ArrayList<>();
		DemonModel d = gd.getWinFristFeats();
		if (d != null && d.getActorId() == actorId) {
			firstReward = cfg.getTopPlayerRewardObjects();
			demon.topPlayerNum += 1;
			// 如果发放保底奖励，则不发第一名的奖励
			if (cfg.getTopPlayerLeastNum() != 0 && demon.topPlayerNum % cfg.getTopPlayerLeastNum() == 0) {
				firstReward = cfg.getTopPlayerLeastRewardObjects();
			}
		}

		// 判断当前角色是否为获胜阵型。是则读取获取功勋总值高的配置。否则反之。
		List<RewardObject> featsTotalReward = new ArrayList<>();
		Actor actor = actorFacade.getActor(actorId);
		if (win.contains(demonModel)) {
			featsTotalReward = cfg.getUpFeatsRewardObjects(actor.level);
		} else {
			featsTotalReward = cfg.getDownFeatsRewardObjects(actor.level);
		}

		// 使用点券产生的奖励
		int useTicketNum = gd.getUseTicketNum(actorId);
		List<RewardObject> ticketRewards = new ArrayList<>();
		if (useTicketNum > 0) {
			ticketRewards = cfg.getConsumeTicketRewards(useTicketNum);
		}

		// 积分计算
		DemonScoreConfig demonScoreConfig = DemonScoreService.getDemonSocreConfig(demonModel.difficult, rank);
		int rewardScore = 0;
		if (demonScoreConfig != null) {
			rewardScore = demonScoreConfig.getScoreNum();
			int playerLessNum = gd.getCampDiffrentSize(demonModel.getCamp());
			int extScore = cfg.getExtraScore(playerLessNum);
			rewardScore += extScore;
			demon.score += rewardScore;

//			Actor actor = actorFacade.getActor(actorId);
			GameOssLogger.demonScoreAdd(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, rewardScore, demon.score);

		}
		demonDao.update(demon);

		// 创建新的demonrank
		DemonRank demonRank = DemonRank.valueOf(actorId);
		DemonHelper.recordDemonRank(demonRank, rank, diff, isWin, demonModel.feats, rewardScore, firstReward, featsRankReward, featsTotalReward, ticketRewards);
		return demonRank;
	}

	private DemonVO demon2demonVO(DemonModel demon) {
		Actor actor = actorFacade.getActor(demon.getActorId());
		IconVO iconVO = iconFacade.getIconVO(demon.getActorId());
		DemonVO demonVO = new DemonVO(demon.getActorId(), actor.actorName, actor.level, demon.feats, DemonCamp.getByCode(demon.getCamp()), demon.getJoinTime(), iconVO);
		return demonVO;
	}

	/**
	 * 保底攻击怪物功勋值
	 * 
	 * @param expression
	 * @param value
	 * @return
	 */
	private long ensureValidFeats(String expression, int value) {
		long addFeats = FormulaHelper.executeCeilInt(expression, value);
		addFeats = addFeats > 0 ? addFeats : 1;
		return addFeats;
	}

	/**
	 * 获取攻击boss时间
	 * 
	 * @param actorId
	 * @return
	 */
	private int getAttackBossTime(long actorId) {
		DemonModel actorDemon = demons.get(actorId);
		DemonConfig cfg = DemonService.get(actorDemon.difficult);
		if (cfg == null) {
			return 0;
		}
		int time = cfg.getInterval() - (TimeUtils.getNow() - actorDemon.attackBossTime);
		time = time > 0 ? time : 0;
		return time;
	}

	/**
	 * 集众降魔是否开始
	 * 
	 * @return
	 */
	private boolean isStart() {
		if (isOpen && openTime != null && openTime.isStart()) {
			return true;
		}
		return false;
	}

	/**
	 * 计算攻击boss额外获得积分
	 * 
	 * @param demonModel
	 * @param cfg
	 * @return 本次获得的降魔积分
	 */
	private int attackBossRewardScore(DemonModel demonModel, DemonConfig cfg) {
		demonModel.attackBossNum += 1;
		int extScore = cfg.getAttackBossExtScore();
		int extNum = cfg.getAttackBossExtScoreNum();
		int extRewardMaxNum = cfg.getAttackBossExtScoreNumMax();
		if (extScore > 0 && extNum > 0 && extRewardMaxNum > 0) {
			if (demonModel.attackBossNum % extNum == 0 && demonModel.attackBossRewardScoreNum < extRewardMaxNum) {
				Demon demon = demonModel.getDemon();
				demon.score += extScore;
				demonDao.update(demon);
				demonModel.attackBossRewardScoreNum += 1;
				demonModel.totalRewardExtScore += extScore;
				return extScore;
			}
		}
		return 0;
	}

	@Override
	public boolean addDemonScore(long actorId, int score) {
		if (score <= 0) {
			return false;
		}
		Demon demon = demonDao.get(actorId);
		demon.score += score;
		demonDao.update(demon);
		return true;
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
				equipFacade.addEquip(actorId, EquipAddType.DEMON_EXCHANGE, id);
			}
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.DEMON_EXCHANGE, id, num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.DEMON_EXCHANGE, id, num);
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

}
