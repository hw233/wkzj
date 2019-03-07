package com.jtang.gameserver.module.crossbattle.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.crossbattle.model.ActorCrossData;
import com.jiatang.common.crossbattle.model.DayEndRewardVO;
import com.jiatang.common.crossbattle.model.ViewLineupVO;
import com.jiatang.common.crossbattle.request.AttackActorResultG2W;
import com.jiatang.common.crossbattle.request.SignupG2W;
import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.HeroAndBuff;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Power;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackPlayerRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.crossbattle.dao.CrossBattleDao;
import com.jtang.gameserver.module.crossbattle.facade.CrossBattleCallbackFacade;
import com.jtang.gameserver.module.crossbattle.helper.CrossBattleWorldPushHelper;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.handler.response.HeroResponse;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.lineup.handler.response.ViewLineupResponse;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.notice.facade.NoticeFacade;
import com.jtang.gameserver.module.power.constant.PowerRule;
import com.jtang.gameserver.module.power.facade.PowerFacade;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.sysmail.type.SysmailType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;

@Component
public class CrossBattleCallbackFacadeImpl implements CrossBattleCallbackFacade, BattleCallBack {
	private static final Logger LOGGER = LoggerFactory.getLogger(CrossBattleCallbackFacadeImpl.class);

	@Autowired
	private BattleFacade battleFacade;

	@Autowired
	private PowerFacade powerFacade;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private LineupFacade lineupFacade;

	@Autowired
	private VipFacade vipFacade;

	@Autowired
	private GoodsFacade goodsFacade;

	@Autowired
	private EquipFacade equipFacade;

	@Autowired
	private HeroSoulFacade heroSoulFacade;
	
	@Autowired
	private SysmailFacade sysmailFacade;
	
	@Autowired
	private NoticeFacade noticeFacade;
	
	@Autowired
	private CrossBattleDao crossBattleDao;

	private Map<Long, BattleResult> battleData = new ConcurrentHashMap<Long, BattleResult>();
	
	private Set<Long> crossBattleActor = new HashSet<>();

	/**
	 * 跨服战是否开始
	 */
	private boolean isStart = false;

	@Override
	public Result attactActorCallBack(long actorId, long targetActorId, byte[] selfFightModel, byte[] targetFightModel, int selfMorale,
			int targetMorale) {
		LineupFightModel actorFightModel = new LineupFightModel(selfFightModel);
		LineupFightModel fightModel = new LineupFightModel(targetFightModel);
		MapConfig map = MapService.get(PowerRule.POWER_RANK_BATTLE_1V1_MAP_ID);

		AttackPlayerRequest attackReq = new AttackPlayerRequest(EventKey.CROSS_BATTLE, map, actorId, actorFightModel, targetActorId, fightModel,
				selfMorale, targetMorale, null, BattleType.CROSS_BATTLE);
		attackReq.continueHP = true;

		Result result = battleFacade.submitAtkPlayerRequest(attackReq, this);
		// 战斗没有开打,前期检查没有通过.
		if (result.isFail()) {
			return Result.valueOf(result.statusCode);
		}

		return Result.valueOf();
	}

	@Override
	public void execute(BattleResult result) {
		AttackPlayerRequest attackReq = (AttackPlayerRequest) result.battleReq;
		long actorId = attackReq.actorId;
		long targetActorId = attackReq.targetActorId;
		battleData.put(actorId, result);

		List<Fighter> fighters = result.attackTeam;
		Map<Integer, Integer> actorHurtMap = new HashMap<Integer, Integer>();
		for (Fighter fighter : fighters) {
			for (HeroVO hero : attackReq.atkTeam.values()) {
				if (hero.heroId == fighter.getHeroId()) {
//					int heroHp = hero.hp;
//					Map<AttackerAttributeKey, Integer> atts = attackReq.getAtkTeamAttrChange().get(fighter.getFighterId());
//					if (atts != null && atts.containsKey(AttackerAttributeKey.HP)) {
//						heroHp += atts.get(AttackerAttributeKey.HP);
//					}
//					if (heroHp > fighter.getHp()){
						actorHurtMap.put(fighter.getHeroId(), fighter.getHert());
//					}
				}
			}
		}
		fighters = result.defendsTeam;
		Map<Integer, Integer> targetHurtMap = new HashMap<Integer, Integer>();
		for (Fighter fighter : fighters) {
			for (HeroVO hero : attackReq.defTeam.values()) {
				if (hero.heroId == fighter.getHeroId()) {
//					int heroHp = hero.hp;
//					Map<AttackerAttributeKey, Integer> atts = attackReq.getDefTeamAttrChange().get(fighter.getFighterId());
//					if (atts != null && atts.containsKey(AttackerAttributeKey.HP)) {
//						heroHp += atts.get(AttackerAttributeKey.HP);
//					}
//					if (heroHp > fighter.getHp()){
						targetHurtMap.put(fighter.getHeroId(), fighter.getHert());
//					}
				}
			}
		}
		

		AttackActorResultG2W attackPlayerResultRequest = new AttackActorResultG2W(targetActorId, actorHurtMap, targetHurtMap);
		CrossBattleWorldPushHelper.attackPlayerResult(actorId, attackPlayerResultRequest);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("跨服请求战斗结果到世界服");
		}
	}

	@Override
	public TResult<SignupG2W> getSignupData(int powerRank) {
		List<Long> list = powerFacade.getRankList(powerRank);
		ArrayList<ActorCrossData> selfServer = new ArrayList<>();

		for (long actorId : list) {
			ActorCrossData crossData = getCrossData(actorId);
			selfServer.add(crossData);
			crossBattleActor.add(actorId);
		}
		SignupG2W signupRequest = new SignupG2W(selfServer);
		CrossBattleWorldPushHelper.signup(signupRequest);
		return TResult.sucess(signupRequest);
	}

	@Override
	public ActorCrossData getCrossData(long actorId) {
		Power power = powerFacade.getPower(actorId);
		Actor actor = actorFacade.getActor(actorId);

		int hp = 0;
		LineupFightModel lineupFightModel = LineupHelper.getLineupFight(actorId);
		for (HeroVO heroVO : lineupFightModel.getHeros().values()) {
			hp += heroVO.getHp();
		}
		for (Map<AttackerAttributeKey, Integer> att : lineupFightModel.getAttributeChanges().values()) {
			if (att.containsKey(AttackerAttributeKey.HP)) {
				hp += att.get(AttackerAttributeKey.HP);
			}
		}
		int vipLevel = vipFacade.getVipLevel(actorId);
		TResult<ViewLineupResponse> result = lineupFacade.getLineupInfo(actorId);
		Map<Integer, HeroAndBuff> lineupHeros = new HashMap<>();
		for (Map.Entry<Integer,HeroResponse> entry : result.item.lineupHeros.entrySet()) {
			int key = entry.getKey();
			HeroAndBuff heroAndBuff = new HeroAndBuff();
			heroAndBuff.hero = entry.getValue().hero;
			heroAndBuff.buffList = entry.getValue().buffList;
			lineupHeros.put(key, heroAndBuff);
		}
		ViewLineupVO viewLineupVO = new ViewLineupVO(lineupHeros , result.item.lineupEquips);
		ActorCrossData actorCrossData = new ActorCrossData(power.rank, actorId, actor.actorName, actor.level, lineupFightModel
				.getBytes(), hp, viewLineupVO.getBytes(), actor.morale, hp, vipLevel);
		return actorCrossData;
	}

	@Override
	public BattleResult pickBattleResult(long actorId) {
		return battleData.remove(actorId);
	}

	@Override
	public void setCrossBattleState(byte start) {
		this.isStart = start == 1 ? true : false;
		if (this.isStart == false) {
			this.crossBattleActor.clear();
		}
	}

	@Override
	public boolean isStart() {
		return this.isStart;
	}

	@Override
	public Result exchangePoint(long actorId, List<RewardObject> list) {
		return sendReward(actorId, list);
	}

	@Override
	public Result attackPlayer() {
		if(isStart()){
			return Result.valueOf();
		}
		return Result.valueOf(GameStatusCodeConstant.CROSS_BATTLE_NOT_START);
	}

	@Override
	public Result dayBattleEndRewardResult(long actorId,DayEndRewardVO endReward) {
		sysmailFacade.sendSysmail(actorId,SysmailType.CROSS_BATTLE_DAY_END,endReward.winServerExtGoods,endReward.serverWinFlag);
		return Result.valueOf();
	}
	
	private Result sendReward(long actorId, List<RewardObject> list) {
		short statusCode = StatusCode.SUCCESS;
		for(RewardObject reward:list){
			switch (reward.rewardType) {
			case GOODS:
				statusCode = goodsFacade.addGoodsVO(actorId, GoodsAddType.CROSS_BATTLE, reward.id, reward.num).statusCode;
				break;
			case EQUIP:
				statusCode = equipFacade.addEquip(actorId, EquipAddType.CROSS_BATTLE, reward.id).statusCode;
				break;
			case HEROSOUL:
				statusCode = heroSoulFacade.addSoul(actorId, HeroSoulAddType.CROSS_BATTLE, reward.id, reward.num).statusCode;
				break;
			default:
				break;
			}
		}
		return Result.valueOf(statusCode);
	}

	@Override
	public void allEndReward(long actorId, String rewardObjects,int serverScoreRank) {
		crossBattleDao.update(rewardObjects);
	}
	
	@Override
	public boolean isInCrossBattle(long actorId) {
		return this.crossBattleActor.contains(actorId);
	}


}
