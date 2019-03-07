package com.jtang.gameserver.module.story.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.ACTOR_LEVEL_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.ALLY_NOT_FIND;
import static com.jiatang.common.GameStatusCodeConstant.BATTLE_NOT_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.BATTLE_NOT_OPEN;
import static com.jiatang.common.GameStatusCodeConstant.FIGHT_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.STORY_NOT_CLEAR;
import static com.jiatang.common.GameStatusCodeConstant.STORY_REWARD_TYPE_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.STORY_STAR_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.UNLOCK_STORY_NOT_FIGHT;
import static com.jiatang.common.GameStatusCodeConstant.VIT_NOT_ENOUGH;
import static com.jtang.core.protocol.StatusCode.TICKET_NOT_ENOUGH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.event.EventBus;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RandomExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.CollectionUtils;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.StoryPassedEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.AwardGoodsConfig;
import com.jtang.gameserver.dataconfig.model.AwardItemConfig;
import com.jtang.gameserver.dataconfig.model.BattleConfig;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.dataconfig.model.StoryBattleRecordConfig;
import com.jtang.gameserver.dataconfig.model.StoryConfig;
import com.jtang.gameserver.dataconfig.model.StoryFightConfig;
import com.jtang.gameserver.dataconfig.model.VipConfig;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dataconfig.service.StoryService;
import com.jtang.gameserver.dataconfig.service.VipService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Stories;
import com.jtang.gameserver.module.adventures.vipactivity.facade.MainHeroFacade;
import com.jtang.gameserver.module.ally.facade.AllyFacade;
import com.jtang.gameserver.module.ally.facade.impl.AllyFacadeImpl;
import com.jtang.gameserver.module.battle.constant.BattleSkipPlayType;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackMonsterRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.constant.GoodsRule;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.model.GoodsVO;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.monster.facade.StoryMonsterFacade;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.story.constant.StoryRule;
import com.jtang.gameserver.module.story.dao.StoryDao;
import com.jtang.gameserver.module.story.facade.StoryFacade;
import com.jtang.gameserver.module.story.handler.response.StoryFightListResponse;
import com.jtang.gameserver.module.story.handler.response.StoryFightResponse;
import com.jtang.gameserver.module.story.handler.response.StoryInfoResponse;
import com.jtang.gameserver.module.story.helper.StoryHelper;
import com.jtang.gameserver.module.story.helper.StoryPushHelper;
import com.jtang.gameserver.module.story.model.AwardType;
import com.jtang.gameserver.module.story.model.StoryBattleResult;
import com.jtang.gameserver.module.story.model.StoryVO;
import com.jtang.gameserver.module.story.type.Star;
import com.jtang.gameserver.module.story.type.StoryRewardType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;
import com.jtang.gameserver.module.user.type.EnergyAddType;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.ReputationAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.module.user.type.VITAddType;
import com.jtang.gameserver.module.user.type.VITDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 故事模块业务接口
 * 
 * @author vinceruan
 * 
 */
@Component
public class StoryFacadeImpl implements StoryFacade, BattleCallBack,ActorLoginListener,ZeroListener {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass()); 

	static String BATTTLE_CONFIG = "BATTILE_ID";
	static String STORY_ID = "STORY_ID";
	static String HERO_MAP = "HERO_MAP";
	static String ALLY_ACTOR_ID = "ALLY_ACTOR_ID"; 
	static String SKIP_BATTLE_ID = "SKIP_BATTLE_ID";

	@Autowired
	StoryDao storyDao;
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	BattleFacade battleFacade;
	@Autowired
	HeroFacade heroFacade;
	@Autowired
	StoryMonsterFacade monsterFacade;
	@Autowired
	HeroSoulFacade heroSoulFacade;
	@Autowired
	EquipFacade equipFacade;
	@Autowired
	GoodsFacade goodsFacade;
	@Autowired
	EventBus eventBus;
	@Autowired
	AllyFacade allyFacade;
	@Autowired
	NotifyFacade notifyFacade;
	@Autowired
	Schedule schedule;
	@Autowired
	PlayerSession playerSession;

	@Autowired
	private VipFacade vipFacade;
	
	@Autowired
	private MainHeroFacade mainHeroFacade;

	@Override
	public Stories get(long actorId) {
		return storyDao.get(actorId);
	}

	@Override
	public Result startBattle(long actorId, int battleId, long allyActorId) {
		BattleConfig battleConfig = StoryService.get(battleId);

		// 战场校验:战场是否存在、是否已经解锁
		Result checkRes = checkBattle(actorId, battleId);
		if (checkRes.isFail()) {
			return checkRes;
		}
		
//		if(allyFacade.isAlly(actorId, allyActorId) == false){
//			//检查盟友是否通关
//			Result allyCheckRes = checkBattle(allyActorId,battleConfig);
//			if(allyCheckRes.isFail()){
//				return Result.valueOf(ALLY_STORY_NOT_CLEAN);
//			}
//		}
		//检查掌教等级
		Actor actor = actorFacade.getActor(actorId);
		if (actor.level < battleConfig.getNeedActorLevel()) {
			return Result.valueOf(ACTOR_LEVEL_ENOUGH);
		}
		// 检查活力
		if (actor.vit < battleConfig.getCostVit()) {
			return Result.valueOf(VIT_NOT_ENOUGH);
		}
		
		
		// 创建队伍,调用战斗接口
		LineupFightModel model = null;
		Map<String, Object> args = new HashMap<String, Object>();
		model = LineupHelper.getLineupFight(actorId);
		args.put(HERO_MAP, model.getHeros());// 只记录自己的英雄
		if (allyActorId < 1) {
			model = LineupHelper.getLineupFight(actorId);
		} else if (allyFacade.isAlly(actorId, allyActorId)) {
			model = LineupHelper.getAllyLineupFight(actorId, allyActorId);
		}

		Map<Integer, MonsterVO> monsterMap = monsterFacade.getMonsters(battleId);
		args.put(BATTTLE_CONFIG, battleConfig);
		args.put(ALLY_ACTOR_ID, allyActorId);
		
		Stories stories = get(actorId);
		int storyId = battleConfig.getStoryId();
		StoryVO storyVO = stories.getStoryMap().get(storyId);
		if (battleConfig.getBattleType() == 0 && getBattleStar(storyVO, battleId) > Star.ZERO_STAR) {
			args.put(SKIP_BATTLE_ID, BattleSkipPlayType.PVE_CAN_SKIP);
		} else {
			if (getBattleStar(storyVO, battleId) == Star.THREE_STAR) {
				args.put(SKIP_BATTLE_ID, BattleSkipPlayType.PVE_CAN_SKIP);
			}
		}
		int morale = actor.morale;
		int monsterMorale = battleConfig.morale;
		MapConfig map = MapService.get(battleConfig.getMapId());
		AttackMonsterRequest event = new AttackMonsterRequest(EventKey.STORY_BATTLE, map, actorId, model, monsterMap, morale, monsterMorale, args,
				BattleType.STORY);
		Result result = battleFacade.submitAtkMonsterRequest(event, this);

		// 战斗没有开打,前期检查没有通过.
		if (result.isFail()) {
			return Result.valueOf(result.statusCode);
		}

		// 扣除活力值
		if(battleConfig.getCostVit() > 0) {
			boolean isOk = actorFacade.decreaseVIT(actorId, VITDecreaseType.STORY, battleConfig.getCostVit());
			if (!isOk) {
				return Result.valueOf(VIT_NOT_ENOUGH);
			}			
		}
		return Result.valueOf();
	}

	/**
	 * @param actorId
	 * @param battleConfig
	 * @param br
	 * @param winLv
	 */
	protected void randomAward(long actorId, BattleConfig battleConfig, StoryBattleResult br) {
		WinLevel winLv = null;
		if(br.fightData == null){
			winLv = WinLevel.BIG_WIN;
		}else{
			winLv = br.fightData.result;
		}
		boolean isLeast = leastGoods(actorId, battleConfig.getBattleId());
		if(isLeast){
			StoryBattleRecordConfig config = StoryService.getLeastReward(battleConfig.getBattleId());
			for(RewardObject rewardObject:config.rewardList){
				switch (rewardObject.rewardType) {
				case GOODS:
					if(br.awardGoods.containsKey((long)rewardObject.id)){//已经有奖励了保底不出,清空保底累计次数
						Stories stories = storyDao.get(actorId);
						stories.battleRecordMap.remove(battleConfig.getBattleId());
					}
					break;
				case HEROSOUL:
					if(br.awardHeroSouls.containsKey(rewardObject.id)){//已经有奖励了保底不出,清空保底累计次数
						Stories stories = storyDao.get(actorId);
						stories.battleRecordMap.remove(battleConfig.getBattleId());
					}
					break;
				default:
					break;
				}
			}
			sendReward(actorId,config.rewardList,br);
			return;
		}

		// 取得物品、装备、魂魄、金币奖励配置
		List<AwardGoodsConfig> goods = battleConfig.getAwardGoodsMap().get(winLv);
		List<AwardGoodsConfig> equips = battleConfig.getAwardEquipMap().get(winLv);
		List<AwardGoodsConfig> heroSouls = battleConfig.getAwardHeroSoulMap().get(winLv);
		Integer golds = battleConfig.getAwardGold().get(winLv);

		Map<Integer, Integer> awardTypeMap = battleConfig.getAwardTypeMap();
		Integer awardType = RandomUtils.randomHit(1000, awardTypeMap);
		if (awardType != null) {
			switch (awardType) {
			case AwardType.EQUIP:
				// 处理装备奖励
				awardEquips(actorId, br, equips);
				break;
			case AwardType.GOLD:
				// 处理金币掉落
				awardGolds(actorId, br, golds);
				break;
			case AwardType.GOODS:
				// 处理物品奖励
				awardGoods(actorId, br, goods);
				break;
			case AwardType.SOUL:
				// 处理魂魄掉落
				awardHeroSouls(actorId, br, heroSouls);
				break;
			}
		}
	}

	private void awardAllys(long actorId, Long allyActorId, BattleConfig battleConfig, WinLevel winLevel) {
		if (allyActorId == null || allyActorId == 0) {
			return;
		}
		int actorLevel = ActorHelper.getActorLevel(actorId);
		int allyLevel = ActorHelper.getActorLevel(allyActorId);
		Map<Integer, Integer> awardGoods = new HashMap<>();
		Integer awardReputation = 0;//合作关卡不产魂魄
		if(actorLevel + StoryRule.ALLY_BATTLE_LEVEL_LIMIT >= allyLevel && allyLevel >= actorLevel - StoryRule.ALLY_BATTLE_LEVEL_LIMIT){// +-掌教 x级盟友才能获得奖励
			// 取得声望、物品、经验、装备、魂魄、金币奖励配置
			List<AwardGoodsConfig> goods = battleConfig.getAwardAllyGoodsMap().get(winLevel);
			int vipLevel = vipFacade.getVipLevel(allyActorId);
			VipConfig config = VipService.getByLevel(vipLevel);
			if(config == null){//vip0按vip算
				config = VipService.getByLevel(1);
			}
			if (CollectionUtils.isNotEmpty(goods)) {
				for (AwardGoodsConfig conf : goods) {
					int rate = conf.getRate();
					if (RandomUtils.is1000Hit(rate)) {
						if(awardGoods.containsKey(conf.getGoodsId())){
							awardGoods.put(conf.getGoodsId(),awardGoods.get(conf.getGoodsId()) + config.getAllyReward(conf.getNum()));
						}else{
							awardGoods.put(conf.getGoodsId(), config.getAllyReward(conf.getNum()));
						}
					}
				}
			}

//			if (awardReputation == null) {
//				awardReputation = 0;
//			}
		}
		if(allyActorId != AllyFacadeImpl.ROBOT_ID && awardGoods.size() > 0){
			Stories stories = storyDao.get(actorId);
			if(stories.allyFightMap.containsKey(allyActorId) == false){
				notifyFacade.createStory(actorId, allyActorId, battleConfig.getBattleId(), winLevel.isWin(), awardReputation, awardGoods,true);
			}else{
				if(stories.allyFightMap.get(allyActorId) < StoryRule.ALLY_FIGHT_REWARD_NUM){
					if(stories.allyFightMap.get(allyActorId) + 1 >= StoryRule.ALLY_FIGHT_REWARD_NUM){
						notifyFacade.createStory(actorId, allyActorId, battleConfig.getBattleId(), winLevel.isWin(), awardReputation, awardGoods,false);
					}else{
						notifyFacade.createStory(actorId, allyActorId, battleConfig.getBattleId(), winLevel.isWin(), awardReputation, awardGoods,true);
					}
				}
			} 
			if(stories.allyFightMap.containsKey(allyActorId)){
				stories.allyFightMap.put(allyActorId, stories.allyFightMap.get(allyActorId) + 1);
			}else{
				stories.allyFightMap.put(allyActorId , 1);
			}
			storyDao.update(stories);
		}
	}

	/**
	 * @param actorId
	 * @param br
	 * @param heroSouls
	 */
	private void awardHeroSouls(long actorId, StoryBattleResult br, List<AwardGoodsConfig> heroSouls) {
		if (CollectionUtils.isNotEmpty(heroSouls)) {
			for (AwardGoodsConfig conf : heroSouls) {
				int rate = conf.getRate();
				int heroId = conf.getGoodsId();
				int num = conf.getNum();
				if (RandomUtils.is1000Hit(rate)) {
					heroSoulFacade.addSoul(actorId, HeroSoulAddType.STORY_AWARD, heroId, num);
					br.awardHeroSouls.put(heroId, num);
				}
			}
		}
	}

	/**
	 * @param actorId
	 * @param br
	 * @param golds
	 */
	private void awardGolds(long actorId, StoryBattleResult br, Integer golds) {
		if (golds != null && golds > 0) {
			actorFacade.addGold(actorId, GoldAddType.STORY, golds);
			if(br.awardGoods.containsKey(Long.valueOf(GoodsRule.GOODS_ID_GOLD))){
				int value = golds + br.awardGoods.get(Long.valueOf(GoodsRule.GOODS_ID_GOLD));
				br.awardGoods.put(Long.valueOf(GoodsRule.GOODS_ID_GOLD),value);
			}else{
				br.awardGoods.put(Long.valueOf(GoodsRule.GOODS_ID_GOLD), golds);
			}
		}
	}

	/**
	 * 处理装备奖励
	 * 
	 * @param actorId
	 * @param br
	 * @param equips
	 */
	private void awardEquips(long actorId, StoryBattleResult br, List<AwardGoodsConfig> equips) {
		if (CollectionUtils.isNotEmpty(equips)) {
			for (AwardGoodsConfig conf : equips) {
				int rate = conf.getRate();
				if (RandomUtils.is1000Hit(rate)) {
					int num = conf.getNum();
					int goodsId = conf.getGoodsId();
					awardEquips(actorId, br, num, goodsId);
				}
			}
		}
	}

	/**
	 * 处理装备奖励
	 * 
	 * @param actorId
	 * @param br
	 * @param num
	 * @param goodsId
	 * @param added
	 */
	protected void awardEquips(long actorId, StoryBattleResult br, int num, int goodsId) {
		int added = 0;
		while (added < num) {
			added++;
			TResult<Long> res = equipFacade.addEquip(actorId, EquipAddType.STORY_AWARD, goodsId);
			if (res.isOk()) {
				br.equips.add(res.item);
			}
		}
	}

	/**
	 * @param goods
	 */
	private void awardGoods(long actorId, StoryBattleResult br, List<AwardGoodsConfig> goods) {
		if (CollectionUtils.isNotEmpty(goods)) {
			for (AwardGoodsConfig conf : goods) {
				int rate = conf.getRate();
				if (RandomUtils.is1000Hit(rate)) {
					TResult<Long> addRes = this.goodsFacade.addGoodsVO(actorId, GoodsAddType.STORY_AWARD, conf.getGoodsId(), conf.getNum());
					if (addRes.isOk()) {
						br.awardGoods.put(addRes.item, conf.getNum());
					}
				}
			}
		}
	}

	/**
	 * @param actorId
	 * @param heroMap
	 * @param result
	 * @param awardExp
	 */
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

	/**
	 * 计算奖励的声望
	 * 
	 * @param actorId
	 * @param battleConfig
	 * @param winLevel
	 * @return
	 */
	private long getReputation(long actorId, BattleConfig battleConfig, WinLevel winLevel) {
		long configReputatoin = battleConfig.getAwardReputation(winLevel);

		if (configReputatoin > 0) {
			Actor actor = this.actorFacade.getActor(actorId);
			int diff = actor.level - battleConfig.getDefaultActorLevel();

			int rate = StoryRule.getRate(diff);
			// // 计算声望损耗
			// for (int[] conf : StoryRule.STORY_REWARD_RULE_LIST) {
			// if (diff > conf[0] && diff < conf[1]) {
			// rate = conf[2];
			// }
			// }
			configReputatoin = configReputatoin * rate / 100;
//			configReputatoin *= getExtReputation(actorId);
			return configReputatoin;
		}
		return 0;
	}

	/**
	 * 战场校验:战场是否存在、是否已经解锁
	 * 
	 * @param stories
	 * @param battleConfig
	 */
	@Override
	public Result checkBattle(long actorId, int battleId) {
		BattleConfig battleConfig = StoryService.get(battleId);
		// 战场配置不存在
		if (battleConfig == null) {
			return Result.valueOf(BATTLE_NOT_EXISTS);
		}

		// 判断战场是否解锁
		BattleConfig preBc = StoryService.get(battleConfig.getDependBattle());
		if (preBc != null) {
			// 如果前一个战场存在并且用户未占领，则当前战场不开放
			Stories stories = get(actorId);
			StoryVO storyVO = stories.getStoryMap().get(preBc.getStoryId());
			if (storyVO == null) {
				return Result.valueOf(BATTLE_NOT_OPEN);
			}

			Byte preBattleStarResult = storyVO.getBattleStar(preBc.getBattleId()); // .battleStarMap.get();
			if (preBattleStarResult == null || preBattleStarResult <= 0) {
				return Result.valueOf(BATTLE_NOT_OPEN);
			}
		}
		return Result.valueOf();
	}

	/**
	 * 处理推送
	 * 
	 * @param actorId
	 *            角色id
	 * @param stories
	 *            故事模型
	 * @param battleConfig
	 *            战场配置
	 * @param br
	 *            战斗结果
	 * @param pushUpdateStoryStar
	 *            是否推送更新故事星星数
	 * @param pushUpdateBattleStar
	 *            是否推送更新战场星数
	 * 
	 */
	private void pushResult(long actorId, Stories stories, BattleConfig battleConfig, StoryBattleResult br, boolean pushUpdateStoryStar,
			boolean pushUpdateBattleStar) {

		int battleId = battleConfig.getBattleId();

		// 推送战斗结果
		StoryPushHelper.pushBattleResult(actorId, br);

		// 推送战场星数
		if (pushUpdateBattleStar) {
			byte battleStar = stories.getStoryMap().get(battleConfig.getStoryId()).getBattleStar(battleId); // .battleStarMap.get(battleId);
			StoryPushHelper.pushBattleStar(actorId, battleId, battleStar);
		}

		// 推送故事星星数
		if (pushUpdateStoryStar) {
			StoryVO stVO = stories.getStoryMap().get(battleConfig.getStoryId());
			StoryPushHelper.pushStoryStar(actorId, battleConfig.getStoryId(), stVO.storyStar);
		}

		// 推送解锁战场
		// if (pushUnlockBattle) {
		// StoryPushHelper.pushOccupiedBattle(actorId, battleId);
		// }
	}
	
	@Override
	public Result clearStoryAward(long actorId, int storyId, int awardType) {
		StoryRewardType storyRewardType = StoryRewardType.getByType(awardType);
		if (storyRewardType == null) {
			return Result.valueOf(STORY_REWARD_TYPE_ERROR);
		}
		
		Stories stories = get(actorId);
		StoryConfig storyConfig = StoryService.getStory(storyId);

		StoryVO storyVO = stories.getStoryMap().get(storyId);
		if (storyVO == null) {
			return Result.valueOf(STORY_NOT_CLEAR);
		}

		// 获取所有的主线任务配置
		List<BattleConfig> list = StoryService.getMainLineBattle(storyId);
		
		//通关奖励
		if (storyRewardType.equals(StoryRewardType.CROSS_REWARD)) {
			if (storyVO.oneStarAwarded == 1) {
				return Result.valueOf(GameStatusCodeConstant.STORY_HAD_AWARDED);
			}
			for (BattleConfig battle : list) {
				int battleId = battle.getBattleId();
				if (storyVO.getBattleStar(battleId) < Star.ONE_STAR) {
					// if (battleRecord.get(battleId) == null ||
					// battleRecord.get(battleId) < 3) {
					return Result.valueOf(GameStatusCodeConstant.AWARD_CONDITION_NOT_REACH);
				}
			}
			sendReward(actorId, storyConfig.getCrossRewardList(),null);
			// 更新到数据库
			stories.addStoryAwardResult(storyId, awardType);
			this.storyDao.update(stories);
			return Result.valueOf();
		}
		
		// 两星奖励
		if (storyRewardType.equals(StoryRewardType.TWO_STAR_REWARD)) {
			if (storyVO.twoStarAwarded == 1) {
				return Result.valueOf(GameStatusCodeConstant.STORY_HAD_AWARDED);
			}
			for (BattleConfig battle : list) {
				int battleId = battle.getBattleId();
				if (storyVO.getBattleStar(battleId) < Star.TWO_STAR) {
					// if (battleRecord.get(battleId) == null ||
					// battleRecord.get(battleId) < 2) {
					return Result.valueOf(GameStatusCodeConstant.AWARD_CONDITION_NOT_REACH);
				}
			}
			sendReward(actorId, storyConfig.getTwoStarRewardList(),null);
			// 记录到数据库
			stories.addStoryAwardResult(storyId, awardType);
			this.storyDao.update(stories);
			return Result.valueOf();
		}
		// 3星奖励
		if (storyRewardType.equals(StoryRewardType.THREE_STAR_REWARD)) {
			if (storyVO.threeStarAwarded == 1) {
				return Result.valueOf(GameStatusCodeConstant.STORY_HAD_AWARDED);
			}
			for (BattleConfig battle : list) {
				int battleId = battle.getBattleId();
				if (storyVO.getBattleStar(battleId) < Star.THREE_STAR) {
					// if (battleRecord.get(battleId) == null ||
					// battleRecord.get(battleId) < 1) {
					return Result.valueOf(GameStatusCodeConstant.AWARD_CONDITION_NOT_REACH);
				}
			}

			sendReward(actorId, storyConfig.getThreeStarRawardList(),null);

			// 更新到数据库
			stories.addStoryAwardResult(storyId, awardType);
			this.storyDao.update(stories);
			return Result.valueOf();
		}
		
		return Result.valueOf(GameStatusCodeConstant.UNKNOWN_AWARD_TYPE);
	}

	/**
	 * 是否第一次打胜该战场
	 * 
	 * @param battleId
	 * @param storyVO
	 * @return
	 */
	private boolean isFirstWinThisBattle(int battleId, StoryVO storyVO, WinLevel level) {
		if (level.isWin() == false) {
			return false;
		}

		// 故事记录没有,故事下面的战场肯定没开打
		if (storyVO == null) {
			return true;
		}

		// 故事记录存在,但是不存在改战场开打记录
		if (!storyVO.containBattleId(battleId)) { // battleStarMap.containsKey(battleId)
			return true;
		}
		Byte lastStar = storyVO.getBattleStar(battleId); // .battleStarMap.get(battleId);
		return StoryHelper.isBattleWin(lastStar) == false;
	}

	/**
	 * 当前战斗结果是否比最近一次在该战场的开打记录好
	 * 
	 * @param battleId
	 * @param storyVO
	 * @param newStar
	 * @return
	 */
	private boolean isBetterResult(int battleId, StoryVO storyVO, int newStar) {
		if (storyVO == null) {
			return true;
		}
		return storyVO.isBetterStar(battleId, newStar);
	}

	private int getBattleStar(StoryVO story, int battleId) {
		if (story == null) {
			return -1;
		}
		Byte star = story.getBattleStar(battleId); // .battleStarMap.get(battleId);
		if (star == null) {
			return -1;
		}

		return star;
	}


	/**
	 * 处理首次胜利奖励
	 * 
	 * @param actorId
	 * @param battleConfig
	 * @param br
	 */
	protected void firstWinAward(long actorId, BattleConfig battleConfig, StoryBattleResult br) {
		AwardItemConfig conf = battleConfig.getFirstWinAwardConfig();
		if (conf != null) {
			int awardType = conf.getAwardType();
			int goodsId = conf.getGoodsId();
			int num = conf.getNum();

			switch (awardType) {
			// 奖励装备
			case AwardItemConfig.AWARD_TYPE_EQUIPS:
				awardEquips(actorId, br, num, goodsId);
				break;
			// 奖励金币
			case AwardItemConfig.AWARD_TYPE_GOLD:
				awardGolds(actorId, br, num);
				break;
			// 奖励物品
			case AwardItemConfig.AWARD_TYPE_GOODS:
				TResult<Long> addRes = this.goodsFacade.addGoodsVO(actorId, GoodsAddType.STORY_AWARD, conf.getGoodsId(), conf.getNum());
				if (addRes.isOk()) {
					br.awardGoods.put(addRes.item, conf.getNum()); // add(addRes.item);
				}
				break;
			// 奖励魂魄
			case AwardItemConfig.AWARD_TYPE_SOUL:
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.STORY_AWARD, goodsId, num);
				br.awardHeroSouls.put(goodsId, num);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 判断战场是否通关
	 */
	@Override
	public TResult<Boolean> isBattlePassed(long actorId, int battleId) {
		BattleConfig conf = StoryService.get(battleId);
		if (conf == null) {
			return TResult.valueOf(BATTLE_NOT_EXISTS);
		}
		Stories story = this.get(actorId);
		StoryVO stVO = story.getStoryMap().get(conf.getStoryId());
		if (stVO == null) {
			return TResult.sucess(Boolean.FALSE);
		}
		Byte battleStar = stVO.getBattleStar(battleId); // .battleStarMap.get(battleId);
		return TResult.sucess(StoryHelper.isBattleWin(battleStar));
	}

	/**
	 * 判断故事是否通关
	 */
	@Override
	public boolean isStoryPassed(long actorId, int storyId) {
		// 获取所有的主线任务配置
		List<BattleConfig> list = StoryService.getMainLineBattle(storyId);
		if (list == null) {
			LOGGER.error("故事配置不存在:[{}]", storyId);
			return false;
		}

		Stories story = this.get(actorId);
		StoryVO stVO = story.getStoryMap().get(storyId);

		// 故事开打记录不存在
		if (stVO == null) {
			return false;
		}

		// 遍历故事下面的每一个关卡，看关卡是否通关
		// Map<Integer, Byte> battleRecs = stVO.battleStarMap;
		for (BattleConfig battle : list) {
			Byte star = stVO.getBattleStar(battle.getBattleId()); // battleRecs.get(battle.getBattleId());
			if (!StoryHelper.isBattleWin(star)) {
				return false;
			}
		}
		return true;
	}

//	/**
//	 * 获取vip特权加成声望
//	 * 
//	 * @param actorId
//	 * @return
//	 */
//	private Integer getExtReputation(long actorId) {
//		int vipLevel = vipFacade.getVipLevel(actorId);
//		Vip4Privilege vip4Privilege = (Vip4Privilege) vipFacade.getVipPrivilege(Vip4Privilege.vipLevel);
//		int battleTimes = battleFacade.getBatteTotalNum(actorId, BattleType.STORY);
//		if (vip4Privilege.getVipLevel() <= vipLevel && (battleTimes == 0 || vip4Privilege.battleTimes == battleTimes)) {
//			return vip4Privilege.rawardNum;
//		}
//		return 1;
//	}

	@Override
	public void execute(BattleResult result) {
		long actorId = result.battleReq.actorId;

		// 失败
		if (result.statusCode != StatusCode.SUCCESS) {
			StoryPushHelper.pushBattleFailResult(actorId, result.statusCode);
			return;
		}

		FightData fightData = result.fightData;
		@SuppressWarnings("unchecked")
		Map<String, Object> args = (Map<String, Object>) result.battleReq.args;
		BattleConfig battleConfig = (BattleConfig) args.get(BATTTLE_CONFIG);
		@SuppressWarnings("unchecked")
		Map<Integer, HeroVO> heroMap = (Map<Integer, HeroVO>) args.get(HERO_MAP);
		Long allyActorId = (Long) args.get(ALLY_ACTOR_ID);

		int battleId = battleConfig.getBattleId();
		Stories stories = get(actorId);
		int storyId = battleConfig.getStoryId();
		StoryVO storyVO = stories.getStoryMap().get(storyId);

		// 进行战后环节处理(加经验、声望、物品等、记录战场开打记录)
		WinLevel winLevel = fightData.result;

		StoryBattleResult br = new StoryBattleResult();
		br.fightData = fightData;

		// 更新战斗数据:是否可以略过PVE(已经3星的关卡可以略过)
		if (args.containsKey(SKIP_BATTLE_ID)) {
			br.fightData.setCanSkipPlay((byte) args.get(SKIP_BATTLE_ID));
		}

		if (isFirstWinThisBattle(battleId, storyVO, winLevel) && battleConfig.getFirstWinAwardConfig() != null) {
			// 首次奖励
			firstWinAward(actorId, battleConfig, br);
		} else {
			// 随机奖励物品、装备、魂魄、金币
			randomAward(actorId, battleConfig, br);
		}
		//额外奖励
		extReward(actorId,battleConfig,br);
		//活动奖励
		List<RewardObject> reawrdList = StoryService.getAppReward(TimeUtils.getNow());
		sendReward(actorId, reawrdList,br);

		// 是否应该推送故事星数(有变化时才推送)
		boolean pushUpdateStoryStar = false;
		// 是否应该推送战场星数(有变化时才推送)
		boolean pushUpdateBattleStar = false;
		if (winLevel.isWin()) {
			// 如果是攻打战场的第一次胜利,则更新已打到哪个战场
			if (isFirstWinThisBattle(battleId, storyVO, winLevel)) {
				// pushUnlockBattle = true;
				stories.setBattleId(battleId);
			}
			// 如果故事是第一次大胜通关并且是最后一个关卡，则发布事件
			byte star = StoryHelper.computeBattleStar(winLevel);
			eventBus.post(new StoryPassedEvent(actorId, storyId, battleId, star, 1, battleConfig.getBattleType()));

			// 结果比最近一次打该战场的结果好,则更新故事星数和战场星数
			if (isBetterResult(battleId, storyVO, star)) {
				pushUpdateStoryStar = true;
				pushUpdateBattleStar = true;
				stories.addBattleResult(storyId, battleId, star);
			}
			
		}
		// 更新到数据库
		this.storyDao.update(stories);

		// 计算声望
		long awardReputation = getReputation(actorId, battleConfig, winLevel);
		br.awardAttribute.put(ActorAttributeKey.REPUTATION.getCode(), awardReputation);

		// 计算仙人经验
		int awardExp = battleConfig.getAwardHeroExp(winLevel);
//		br.awardHeroExp = awardExp;
		// 添加所有上仙仙人经验
		br.awardHeroExp = awardHeroExp(actorId, heroMap, result, awardExp);

		// 推送结果
		pushResult(actorId, stories, battleConfig, br, pushUpdateStoryStar, pushUpdateBattleStar);

		// 添加奖励的声望
		actorFacade.addReputation(actorId, ReputationAddType.STORY, awardReputation);


		// 添加给盟友的奖励
		awardAllys(actorId, allyActorId, battleConfig, winLevel);

		// 添加物品掉落
		List<GoodsVO> list = result.dropedGoods.get(actorId);
		if (list != null) {
			for (GoodsVO goods : list) {
				this.goodsFacade.addGoodsVO(actorId, GoodsAddType.STORY_DROP, goods.goodsId, goods.num);
			}
		}

		// 处理金币掉落
		Integer gold = result.golds.get(actorId);
		if (gold != null) {
			actorFacade.addGold(actorId, GoldAddType.STORY, gold);
		}
		
		// 添加oss日志
		Actor actor = actorFacade.getActor(actorId);
		GameOssLogger.storyBattle(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, battleId, winLevel);
		
	}
	
	private void extReward(long actorId, BattleConfig battleConfig,StoryBattleResult br) {
		List<RandomExprRewardObject> reward = battleConfig.extRewardList;
		if(reward.size() == 0){
			return;
		}
		Map<Integer,Integer> map = new HashMap<>();
		for (int i = 0; i < reward.size(); i++) {
			map.put(i, reward.get(i).rate);
		}
		Integer index = RandomUtils.randomHit(1000, map);
		if(index == null){
			return;
		}
		RandomExprRewardObject extReward = reward.get(index);
		switch (extReward.rewardType) {
		case GOLD:
			awardGolds(actorId, br, extReward.num);
			break;
		case GOODS:
			TResult<Long> addRes = goodsFacade.addGoodsVO(actorId, GoodsAddType.STORY_AWARD, extReward.id,extReward.num);
			if(br.awardGoods.containsKey(addRes.item)){
				int value = extReward.num + br.awardGoods.get(addRes.item);
				br.awardGoods.put(addRes.item, value);
			}else{
				br.awardGoods.put(addRes.item, extReward.num);
			}
			break;
		default:
			break;
		}
	}


	@Override
	public TResult<StoryFightListResponse> storyFight(long actorId,long allyId, int fightNum,int battleId) {
		BattleConfig battleConfig = StoryService.get(battleId);
		if (battleConfig == null) {
			return TResult.valueOf(BATTLE_NOT_OPEN);
		}
		Actor allys = actorFacade.getActor(allyId);
		if(battleConfig.getBattleType() == 0 && allys == null){
			return TResult.valueOf(ALLY_NOT_FIND);
		}
		Stories stories = get(actorId);
		StoryVO storyVO = stories.getStoryMap().get(StoryRule.MUTI_FIGHT_STORY_ID);
		if (storyVO == null) {
			return TResult.valueOf(UNLOCK_STORY_NOT_FIGHT);
		}
		
		// 战场校验:战场是否存在、是否已经解锁
		Result checkRes = checkBattle(actorId, battleId);
		if (checkRes.isFail()) {
			return TResult.valueOf(checkRes.statusCode);
		}
		
		//检查星级
		stories = get(actorId);
		int storyId = battleConfig.getStoryId();
		storyVO = stories.getStoryMap().get(storyId);
		byte star = storyVO.getBattleStar(battleId);
		if(star < StoryRule.STORY_FIGHT_STAR){
			return TResult.valueOf(STORY_STAR_NOT_ENOUGH);
		}
		
		// 检查活力
		Actor actor = actorFacade.getActor(actorId);
		if (actor.vit < battleConfig.getCostVit() * fightNum) {
			return TResult.valueOf(VIT_NOT_ENOUGH);
		}
		StoryFightConfig config = StoryService.getFightConfig();
		int goodsNum = goodsFacade.getCount(actorId, config.goodsId);
		if(goodsNum < fightNum){//物品不足
			return TResult.valueOf(FIGHT_NOT_ENOUGH);
		}else{
			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.STORY_FIGHT, config.goodsId, fightNum);
		}
		actorFacade.decreaseVIT(actorId, VITDecreaseType.STORY, battleConfig.getCostVit() * fightNum);
		
		List<StoryFightResponse> list = new ArrayList<>();
		for (int i = 0; i < fightNum; i++) {
			StoryFightResponse response = new StoryFightResponse();
			StoryBattleResult battleResult = new StoryBattleResult();
			randomAward(actorId, battleConfig, battleResult);//物品掉落
			
			long awardReputation = getReputation(actorId, battleConfig, WinLevel.BIG_WIN);
			battleResult.awardAttribute.put(ActorAttributeKey.REPUTATION.getCode(), awardReputation);//掌教声望
			extReward(actorId,battleConfig,battleResult);
			
//			int awardExp = battleConfig.getAwardHeroExp(WinLevel.BIG_WIN);//仙人经验
			battleResult.awardHeroExp = new HashMap<>();
			
			// 添加奖励的声望
			actorFacade.addReputation(actorId, ReputationAddType.STORY, awardReputation);

//			LineupFightModel model = LineupHelper.getLineupFight(actorId);
//			Map<Integer,HeroVO> heroMap = model.getHeros();
//			 添加所有上阵仙人经验
//			awardHeroExp(actorId, heroMap, null, awardExp);
			//活动奖励
			List<RewardObject> reawrdList = StoryService.getAppReward(TimeUtils.getNow());
			sendReward(actorId, reawrdList,battleResult);
			response.awardAttribute = battleResult.awardAttribute;
			response.awardGoods = battleResult.awardGoods;
			response.awardHeroExp = battleResult.awardHeroExp;
			response.awardHeroSouls = battleResult.awardHeroSouls;
			response.equips = battleResult.equips;
			response.storyFightReward.addAll(battleConfig.battleRewardList);
			sendReward(actorId, battleConfig.battleRewardList,null);
			list.add(response);
			awardAllys(actorId, allyId, battleConfig, WinLevel.BIG_WIN);
		}
		eventBus.post(new StoryPassedEvent(actorId, storyId, battleId, star, fightNum, battleConfig.getBattleType()));
		return TResult.sucess(new StoryFightListResponse(list));
	}

	@Override
	public Result buyFightGoods(long actorId) {
		Stories stories = storyDao.get(actorId);
		ChainLock lock = LockUtils.getLock(stories);
		try{
			lock.lock();
			StoryFightConfig config = StoryService.getFightConfig();
			int num = config.getCostTicket(stories.num);
			boolean result = vipFacade.decreaseTicket(actorId, TicketDecreaseType.BUY_FIGHT, num, 0, 0);
			if(result == false){
				return Result.valueOf(TICKET_NOT_ENOUGH);
			}
			stories.num ++;
			stories.buyTime = TimeUtils.getNow();
			goodsFacade.addGoodsVO(actorId, GoodsAddType.BUY_FIGHT, config.goodsId,config.num);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}
	
	@Override
	public TResult<StoryInfoResponse> getFightInfo(long actorId) {
		Stories stories = storyDao.get(actorId);
		StoryFightConfig config = StoryService.getFightConfig();
		int num = config.getCostTicket(stories.num);
		int goodsNum = goodsFacade.getCount(actorId, config.goodsId);
		StoryInfoResponse response = new StoryInfoResponse(num,goodsNum);
		return TResult.sucess(response);
	}

	@Override
	public void onLogin(long actorId) {
		Stories stories = storyDao.get(actorId);
		ChainLock lock = LockUtils.getLock(stories);
		try{
			lock.lock();
			if(DateUtils.isToday(stories.buyTime) == false){
				stories.buyTime = TimeUtils.getNow();
				stories.num = 0;
				stories.allyFightMap.clear();
				storyDao.update(stories);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		
	}

	@Override
	public void onZero() {
		Set<Long> actorIds = playerSession.onlineActorList();
		for(Long actorId : actorIds){
			Stories stories = storyDao.get(actorId);
			ChainLock lock = LockUtils.getLock(stories);
			try{
				lock.lock();
				stories.buyTime = TimeUtils.getNow();
				stories.num = 0;
				stories.allyFightMap.clear();
				storyDao.update(stories);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}
	
	/**
	 * 发放奖励
	 * 
	 * @param actorId
	 * @param id
	 * @param num
	 * @param rewardType
	 */
	private void sendReward(long actorId, int id, int num, RewardType rewardType,StoryBattleResult br) {
		switch (rewardType) {
		case EQUIP: {
			for (int i = 0; i < num; i++) {
				long uuid = equipFacade.addEquip(actorId, EquipAddType.STORY_AWARD, id).item;
				if(br != null){
					br.equips.add(uuid);
				}
			}
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.STORY_AWARD, id, num);
			if(br != null){
				br.awardHeroSouls.put(id, num);
			}
			break;
		}
		case GOODS: {
			long uuid = goodsFacade.addGoodsVO(actorId, GoodsAddType.STORY_AWARD, id, num).item;
			if(br != null){
				br.awardGoods.put(uuid, num);
			}
			break;
		}
		case GOLD:{
			actorFacade.addGold(actorId, GoldAddType.STORY, num);
			if(br != null){
				br.awardGoods.put(Long.valueOf(GoodsRule.GOODS_ID_GOLD), num);
			}
		}
		break;
		case TICKET:{
			vipFacade.addTicket(actorId, TicketAddType.STORY, num);
		}
		break;
		case VIT:{
			actorFacade.addVIT(actorId, VITAddType.STORY_REWARD, num);
		}
		break;
		case ENERGY:{
			actorFacade.addEnergy(actorId, EnergyAddType.STORY_AWARD, num);
		}
		break;
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
	private void sendReward(long actorId, List<RewardObject> list,StoryBattleResult br) {
		for (RewardObject rewardObject : list) {
			sendReward(actorId, rewardObject.id, rewardObject.num, rewardObject.rewardType,br);
		}
	}
	
	/**
	 * 物品保底
	 * @param actorId
	 * @param battleId
	 * @return
	 */
	public boolean leastGoods(long actorId, int battleId) {
		if (StoryService.hasGoods(battleId) == false) {
			return false;
		}
		StoryBattleRecordConfig cfg = StoryService.getLeastReward(battleId);
		if (cfg == null) {
			return false;
		}
		if (cfg.numberOfUseMax == cfg.numberOfUseMin && cfg.numberOfUseMax == 0 && cfg.numberOfUseMin == 0) {
			return false;
		}
		if (cfg.numberOfUseMax < cfg.numberOfUseMin) {
			return false;
		}
		Stories stories = storyDao.get(actorId);
		int useNum = 0;
		if (stories.battleRecordMap.containsKey(battleId)) {
			useNum = stories.battleRecordMap.get(battleId);
		} 
		useNum += 1;
		
		int leastNum = stories.getLeastNum(battleId);
		if (leastNum == 0) { //计算保底次数
			int maxValue = cfg.numberOfUseMax;
			int minValue = cfg.numberOfUseMin;
			minValue = Math.max(minValue, useNum);
			leastNum = RandomUtils.nextInt(minValue, maxValue);
			if (leastNum == 0) {
				return false;
			}
			stories.setLeastNum(battleId, leastNum);
		}
		
		
		if (useNum % leastNum == 0) {
			useNum = 0;
			stories.battleRecordMap.put(battleId, useNum);
			storyDao.update(stories);
			stories.setLeastNum(battleId, 0);
			return true;
		} else {
			stories.battleRecordMap.put(battleId, useNum);
			storyDao.update(stories);
			return false;
		}
		
	}

}
