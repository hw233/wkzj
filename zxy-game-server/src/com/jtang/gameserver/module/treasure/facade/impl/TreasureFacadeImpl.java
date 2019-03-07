package com.jtang.gameserver.module.treasure.facade.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.dataconfig.model.RewardConfig;
import com.jtang.gameserver.dataconfig.model.TreasureBattleConfig;
import com.jtang.gameserver.dataconfig.model.TreasureExchangeConfig;
import com.jtang.gameserver.dataconfig.model.TreasureGlobalConfig;
import com.jtang.gameserver.dataconfig.model.TreasureLevelConfig;
import com.jtang.gameserver.dataconfig.model.TreasureMonsterConfig;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dataconfig.service.MonsterService;
import com.jtang.gameserver.dataconfig.service.TreasureService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Treasure;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackMonsterRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.chat.facade.ChatFacade;
import com.jtang.gameserver.module.demon.model.OpenTime;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.model.GoodsVO;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.story.helper.StoryHelper;
import com.jtang.gameserver.module.treasure.dao.TreasureDao;
import com.jtang.gameserver.module.treasure.facade.TreasureFacade;
import com.jtang.gameserver.module.treasure.handler.response.TreasureFightResponse;
import com.jtang.gameserver.module.treasure.helper.TreasurePushHelper;
import com.jtang.gameserver.module.treasure.model.GridVO;
import com.jtang.gameserver.module.treasure.model.TreasureBattleResult;
import com.jtang.gameserver.module.treasure.model.TreasureVO;
import com.jtang.gameserver.module.treasure.type.MoveType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;
import com.jtang.gameserver.module.user.type.ReputationAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class TreasureFacadeImpl implements TreasureFacade, ActorLoginListener, BattleCallBack, ApplicationListener<ContextRefreshedEvent> {

	private static final String GRIDVO = "GRIDVO";
	private static final String HERO_MAP = "HERO_MAP";
	private static final String STEP = "STEP";
	private static final String BATTLE_ID = "BATTLE_ID";

	@Autowired
	private TreasureDao treasureDao;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private BattleFacade battleFacade;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade; 
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private ChatFacade chatFacade;
	@Autowired
	private Schedule schedule;
	@Autowired
	private PlayerSession playerSession;

	/**
	 * 是否开放
	 */
	private boolean isOpen = false;

	/**
	 * 正在使用的时间
	 */
	private OpenTime openTime;

	@Override
	public void onLogin(long actorId) {
		reset(actorId);
		if (isOpen == true && openTime != null) {
			TreasurePushHelper.pushTreasureState(actorId, 1);
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(new Runnable() {
			@Override
			public void run() {
				if (isOpen == false && openTime == null) { // 未开放时才可开放
					List<OpenTime> list = TreasureService.getOpenTimes();
					for (OpenTime time : list) {// 取一个开放时间
						if (time.isStart()) {
							Set<Long> actorIds = playerSession.onlineActorList();
							for (Long actorId : actorIds) {
								TreasurePushHelper.pushTreasureState(actorId, 1);// 推送迷宫开启状态
							}
							openTime = time;
							isOpen = true;
						}
					}
				}

				if (isOpen == true && openTime != null) {// 开放时才可关闭
					if (openTime.isStart() == false) {
						Set<Long> actorIds = playerSession.onlineActorList();
						for (Long actorId : actorIds) {
							TreasurePushHelper.pushTreasureState(actorId, 0);// 推送迷宫关闭状态
						}
						openTime = null;
						isOpen = false;
						treasureDao.clean();
					}
				}
			}
		}, 1);
	}

	@Override
	public TResult<TreasureVO> getTreasure(long actorId) {
		if (isStart() == false) {
			return TResult.valueOf(GameStatusCodeConstant.TREASURE_NOT_OPEN);
		}
		Treasure treasure = treasureDao.getTreasure(actorId);
		TreasureGlobalConfig config = TreasureService.getMazeConfig();
		if(treasure.useNum >= config.maxStep){
			return TResult.valueOf(GameStatusCodeConstant.TREASURE_CLOSE);
		}
		reset(actorId);
		int level = ActorHelper.getActorLevel(actorId);
		TreasureVO treasureVO = treasureDao.getTreasureVO(actorId, level);
		treasureVO.useNum = treasure.useNum;
		treasureVO.getExchangeNum = FormulaHelper.executeCeilInt(config.getReward().num, level);
		treasureVO.costTicket = FormulaHelper.executeCeilInt(config.costTicket, level);
		return TResult.sucess(treasureVO);
	}

	@Override
	public Result move(long actorId) {
		reset(actorId);
		if (isStart() == false) {
			return Result.valueOf(GameStatusCodeConstant.TREASURE_NOT_OPEN);
		}

		TreasureVO treasureVO = treasureDao.getTreasureVO(actorId, ActorHelper.getActorLevel(actorId));
		Treasure treasure = treasureDao.getTreasure(actorId);
		if (treasureVO.isMoveOver()) {// 格子已经全部走完了
			return Result.valueOf(GameStatusCodeConstant.GRID_MOVE_OVER);
		}
		TreasureGlobalConfig mazeConfig = TreasureService.getMazeConfig();
		if(treasure.useNum >= mazeConfig.maxStep){
			return Result.valueOf(GameStatusCodeConstant.TREASURE_CLOSE);
		}

		int level = actorFacade.getActor(actorId).level;// 玩家等级
		if (treasure.useNum >= mazeConfig.count) {// 免费次数使用完毕,扣除点券
			int costTicket = FormulaHelper.executeCeilInt(mazeConfig.costTicket, level);
			boolean isSuccess = vipFacade.decreaseTicket(actorId, TicketDecreaseType.MAZE_TREASURE, costTicket, 0, 0);
			if (isSuccess == false) {
				return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
			}
			treasure.useNum += 1;
			treasureVO.useNum = treasure.useNum;
		} else {
			treasure.useNum += 1;
			treasureVO.useNum = treasure.useNum;
		}
		RewardType stepRewardType = RewardType.getType(mazeConfig.getReward().type);
		int stepNum = FormulaHelper.executeCeilInt(mazeConfig.getReward().num, level);
		sendReward(actorId, stepRewardType, mazeConfig.getReward().rewardId, stepNum, null);
		treasureDao.update(treasure);
		Entry<MoveType, Integer> step = getLeast(treasureVO,treasure,mazeConfig);//保底
		if(step == null){
			step = getDirection(treasureVO);// 获得下一步
		}

		String gridKey = TreasureService.parseKey(treasureVO.gridX, treasureVO.gridY);
		GridVO gridVO = treasureVO.map.get(gridKey);
		if (gridVO.state == 1) {// 格子已经走过了
			int exchangeNum = exchangeGoods(actorId).item;
			TreasureFightResponse treasureFightResponse = new TreasureFightResponse(0, gridVO, step, treasure.useNum, null, exchangeNum);
			TreasurePushHelper.pushBattleResult(actorId, treasureFightResponse);
			return Result.valueOf();
		}

		if (gridVO.isMonster()) {// 需要打怪物
			TreasureLevelConfig treasureLevelConfig = TreasureService.getMonsterByLevel(level);// 怪物配置
			TreasureBattleConfig battleConfig = TreasureService.getBattleConfig(treasureLevelConfig.battleId);// 战场配置

			Map<String, Object> args = new HashMap<String, Object>();
			args.put(GRIDVO, gridVO);
			LineupFightModel atkLineup = LineupHelper.getLineupFight(actorId);// 玩家阵容
			args.put(HERO_MAP, atkLineup.getHeros());
			args.put(STEP, step);
			args.put(BATTLE_ID, battleConfig.battleId);

			Map<Integer, Integer> monsters = treasureLevelConfig.getMonster();// 怪物配置
			Map<Integer, MonsterVO> monsterVOs = new HashMap<Integer, MonsterVO>();// 怪物阵容
			for (Integer key : monsters.keySet()) {
				monsterVOs.put(key, new MonsterVO(MonsterService.get(monsters.get(key))));
			}
			Map<Long, Map<AttackerAttributeKey, Integer>> defTeamAttrChange = new HashMap<>();// 怪物附加属性
			for (MonsterVO monsterVO : monsterVOs.values()) {
				TreasureMonsterConfig treausreMonsterConfig = TreasureService.getMonsterExpr(monsterVO.getHeroId());
				Map<AttackerAttributeKey, Integer> attributeMap = new HashMap<>();
				int hp = FormulaHelper.executeCeilInt(treausreMonsterConfig.hpExpr, level);
				int attack = FormulaHelper.executeCeilInt(treausreMonsterConfig.attackExpr, level);
				int defense = FormulaHelper.executeCeilInt(treausreMonsterConfig.defenseExpr, level);
				attributeMap.put(AttackerAttributeKey.HP, hp);
				attributeMap.put(AttackerAttributeKey.ATK, attack);
				attributeMap.put(AttackerAttributeKey.DEFENSE, defense);
				defTeamAttrChange.put(monsterVO.getSpriteId(), attributeMap);
			}

			Actor actor = actorFacade.getActor(actorId);
			int actorMorale = actor.morale;// 玩家气势
			int mosterMorale = FormulaHelper.executeCeilInt(battleConfig.morale, level);// 怪物气势
			AttackMonsterRequest event = new AttackMonsterRequest(EventKey.TREASURE_BATTLE, MapService.get(battleConfig.mapId), actorId, atkLineup,
					monsterVOs, actorMorale, mosterMorale, null, defTeamAttrChange, args, BattleType.TREASURE_BATTLE);
			Result result = battleFacade.submitAtkMonsterRequest(event, this);
			if (result.isFail()) {
				return Result.valueOf(GameStatusCodeConstant.FIGHT_ERROR);
			}
		} else {// 不需要打怪物,直接发放奖励
			gridVO.state = 1;
			RewardType rewardType = gridVO.rewardObject.rewardType;
			int rewardId = gridVO.rewardObject.id;
			int num = gridVO.rewardObject.num;
			sendReward(actorId, rewardType, rewardId, num, new TreasureBattleResult());
			if (gridVO.isBigGift()) {// 如果是大奖,发送系统公告
				if(treasure.isFirstUse()){
					treasure.isFirstUse = 0;
					treasureDao.update(treasure);
				}
				RewardObject rewardObject = new RewardObject(rewardType, rewardId, num);
				chatFacade.sendMazeTreasureChat(actorId, rewardObject);
			}
			int exchangeNum = exchangeGoods(actorId).item;
			TreasureFightResponse treasureFightResponse = new TreasureFightResponse(0, gridVO, step, treasure.useNum, null, exchangeNum);
			TreasurePushHelper.pushBattleResult(actorId, treasureFightResponse);
		}
		return Result.valueOf();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(BattleResult result) {
		FightData fightData = result.fightData;
		long actorId = result.battleReq.actorId;
		Treasure treasure = treasureDao.getTreasure(actorId);
		Map<String, Object> args = (Map<String, Object>) result.battleReq.args;
		GridVO gridVO = (GridVO) args.get(GRIDVO);
		Map<Integer, HeroVO> heroMap = (Map<Integer, HeroVO>) args.get(HERO_MAP);
		Entry<MoveType, Integer> step = (Entry<MoveType, Integer>) args.get(STEP);
		int battleId = (Integer) args.get(BATTLE_ID);
		byte battleStar = 0;
		TreasureBattleResult treasureBattleResult = new TreasureBattleResult();
		if (fightData.result.isWin()) {// 胜利
			WinLevel winLevel = fightData.result;
			Actor actor = actorFacade.getActor(actorId);
			battleStar = StoryHelper.computeBattleStar(fightData.result);

			// 关卡奖励
			TreasureBattleConfig treasureBattleConfig = TreasureService.getBattleConfig(battleId);
			RewardConfig reward = treasureBattleConfig.getReward();
			if (reward != null) {
				RewardType rewardType = RewardType.getType(reward.type);
				int rewardNum = FormulaHelper.executeCeilInt(reward.num, actor.level);
				sendReward(actorId, rewardType, reward.rewardId, rewardNum, treasureBattleResult);
			}

			// 关卡经验、声望
			// 计算声望
			int awardReputation = getReputation(actorId, treasureBattleConfig, winLevel);
			treasureBattleResult.awardAttribute.put(ActorAttributeKey.REPUTATION.getCode(), awardReputation);
			actorFacade.addReputation(actorId, ReputationAddType.TREASURE, awardReputation);

			// 计算仙人经验
			int awardExp = FormulaHelper.executeCeilInt(treasureBattleConfig.getHeroExp(winLevel), actor.level);
			treasureBattleResult.addHeroExp = awardExp;
			awardHeroExp(actorId, heroMap, result, awardExp);

			// 加格子的奖励
			RewardObject rewardObject = gridVO.rewardObject;
			sendReward(actorId, rewardObject.rewardType, rewardObject.id, rewardObject.num, null);
			TreasureVO treasureVO = treasureDao.getTreasureVO(actorId, actor.level);
			gridVO.state = 1;
			if (gridVO.isBigGift()) {// 如果是大奖,发送系统公告
				if(treasure.isFirstUse()){
					treasure.isFirstUse = 0;
					treasureDao.update(treasure);
				}
				chatFacade.sendMazeTreasureChat(actorId, new RewardObject(rewardObject.rewardType, rewardObject.id, rewardObject.num));
			}
			treasureVO.map.put(TreasureService.parseKey(gridVO.gridX, gridVO.gridY), gridVO);// 更新格子状态
		}
		int exchangeNum = exchangeGoods(actorId).item;
		treasureBattleResult.fightData = fightData;
		treasureBattleResult.battleStar = battleStar;
		TreasureFightResponse treasureFightResponse = new TreasureFightResponse(1, gridVO, step, treasure.useNum, treasureBattleResult, exchangeNum);
		TreasurePushHelper.pushBattleResult(actorId, treasureFightResponse);
	}

	/**
	 * 获取下一步,并赋值到玩家现在格子
	 * 
	 * @param treasureVO
	 * @return
	 */
	private Entry<MoveType, Integer> getDirection(TreasureVO treasureVO) {
		TreasureGlobalConfig mazeConfig = TreasureService.getMazeConfig();
		List<MoveType> moveTypeList = new ArrayList<>(Arrays.asList(MoveType.values()));
		Map<MoveType, Integer> moveMap = new HashMap<>();
		int gridX = treasureVO.gridX;
		int gridY = treasureVO.gridY;
		int beginIndex = mazeConfig.beginIndex;
		int endIndex = mazeConfig.endIndex;

		// 判断只能往哪边走
		if (gridX == beginIndex) {
			moveTypeList.remove(MoveType.LEFT);
		}

		if (gridX == endIndex) {
			moveTypeList.remove(MoveType.RIGHT);
		}

		if (gridY == beginIndex) {
			moveTypeList.remove(MoveType.UP);
		}

		if (gridY == endIndex) {
			moveTypeList.remove(MoveType.DOWN);
		}

		// 获取能走的方向可以随机走几步
		for (MoveType key : moveTypeList) {
			int step = 0;
			switch (key) {
			case LEFT:
				step = RandomUtils.nextInt(1, gridX - beginIndex);
				break;
			case RIGHT:
				step = RandomUtils.nextInt(1, endIndex - gridX);
				break;
			case UP:
				step = RandomUtils.nextInt(1, gridY - beginIndex);
				break;
			case DOWN:
				step = RandomUtils.nextInt(1, endIndex - gridY);
				break;
			}
			moveMap.put(key, step);
		}

		// 随机取一个方向走几步
		int index = RandomUtils.nextIntIndex(moveTypeList.size());
		MoveType moveType = moveTypeList.get(index);
		Entry<MoveType, Integer> step = null;
		for (Entry<MoveType, Integer> entry : moveMap.entrySet()) {
			if (entry.getKey() == moveType) {
				switch (entry.getKey()) {
				case LEFT:
					gridX -= entry.getValue();
					break;
				case RIGHT:
					gridX += entry.getValue();
					break;
				case UP:
					gridY -= entry.getValue();
					break;
				case DOWN:
					gridY += entry.getValue();
					break;
				}
				step = entry;
			}
		}
		treasureVO.gridX = gridX;
		treasureVO.gridY = gridY;
		return step;

	}

	/**
	 * 发放奖励
	 */
	private void sendReward(long actorId, RewardType rewardType, int id, int number, TreasureBattleResult treasureBattleResult) {
		long uuid = 0;
		switch (rewardType) {
		case GOODS:
			uuid = goodsFacade.addGoodsVO(actorId, GoodsAddType.MAZE_TREASURE, id, number).item;
			if (treasureBattleResult != null) {
				treasureBattleResult.awardGoods.put(uuid, number);
			}
			break;
		case EQUIP:
			uuid = equipFacade.addEquip(actorId, EquipAddType.MAZE_TREASURE, id).item;
			if (treasureBattleResult != null) {
				treasureBattleResult.equipList.add(uuid);
			}
			break;
		case HEROSOUL:
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.MAZE_TREASURE, id, number);
			if (treasureBattleResult != null) {
				treasureBattleResult.herosoulMap.put(id, number);
			}
			break;
		default:
			break;
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
	private int getReputation(long actorId, TreasureBattleConfig treasureBattleConfig, WinLevel winLevel) {
		int actorLevel = ActorHelper.getActorLevel(actorId);
		int configReputatoin = FormulaHelper.executeCeilInt(treasureBattleConfig.getReputation(winLevel), actorLevel);
		return configReputatoin;
	}

	/**
	 * 添加仙人经验
	 * 
	 * @param actorId
	 * @param heroMap
	 * @param result
	 * @param awardExp
	 */
	private void awardHeroExp(long actorId, Map<Integer, HeroVO> heroMap, BattleResult result, Integer awardExp) {
		Map<Integer, List<String>> map = result.addExpExpr.get(actorId);
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
		}
	}

	/**
	 * 迷宫寻宝是否已经开启
	 * 
	 * @return
	 */
	private boolean isStart() {
		if (isOpen && openTime != null && openTime.isStart()) {
			return true;
		}
		return false;
	}

	@Override
	public TResult<Integer> exchangeReward(long actorId, int exchangeId, int num) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		TreasureExchangeConfig config = TreasureService.getExchangeConfig(exchangeId);
		if (config == null) {
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		if (num <= 0) {
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		Result result = goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.TREASURE_DECREASE, config.deductId, config.deductNum * num);
		if (result.isFail()) {
			return TResult.valueOf(GameStatusCodeConstant.TREASURE_EXCHANGE_GOODS_ERROR);
		}
		GoodsVO goodsVO = goodsFacade.getGoodsVO(actorId, config.deductId);
		int exchangeNum = goodsVO == null ? 0 : goodsVO.num;
		RewardObject rewardObject = config.getReward();
		int rewardNum = num * rewardObject.num;
		sendExchangeReward(actorId, rewardObject.rewardType, rewardObject.id, rewardNum, null);
		return TResult.sucess(exchangeNum);
	}

	/**
	 * 获取玩家兑换物品剩余数量
	 * 
	 * @param actorId
	 * @return
	 */
	public TResult<Integer> exchangeGoods(long actorId) {
		int goodsId = TreasureService.getExchangeGoodsId();
		GoodsVO goodsVO = goodsFacade.getGoodsVO(actorId, goodsId);
		if (goodsVO == null) {
			return TResult.sucess(0);
		}
		return TResult.sucess(goodsVO.num);
	}
	
	private void reset(long actorId){
		Treasure treasure = treasureDao.getTreasure(actorId);
		if (DateUtils.isToday(treasure.operationTime) == false) {
			int level = ActorHelper.getActorLevel(actorId);
			TreasureVO treasureVO = treasureDao.getTreasureVO(actorId, level);
			treasureVO.reset(level);// 重置迷宫数据
			treasure.reset();// 重置玩家使用数据
			treasureDao.update(treasure);
		}
	}
	
	/**
	 * 获取保底
	 * @param treasureVO
	 * @return
	 */
	private Entry<MoveType, Integer> getLeast(TreasureVO treasureVO,Treasure treasure,TreasureGlobalConfig config) {
		for(GridVO gridVO:treasureVO.map.values()){//大奖已经领过了,不需要保底
			if(gridVO.isBigGift() && gridVO.isGet()){
				return null;
			}
		}
		if(treasureVO.step.isEmpty() == false){
			Entry<MoveType, Integer> step = null;
			treasureVO.step = treasureVO.step;
			for(Entry<MoveType,Integer> entry : treasureVO.step.entrySet()){
				step = entry;
				break;
			}
			if(step != null){
				switch(step.getKey()){
				case DOWN:
					treasureVO.gridY += step.getValue();
					break;
				case LEFT:
					treasureVO.gridX -= step.getValue();
					break;
				case RIGHT:
					treasureVO.gridX += step.getValue();
					break;
				case UP:
					treasureVO.gridY -= step.getValue();
					break;
				default:
					break;
				}
				treasureVO.step.remove(step.getKey());
				return step;
			}
		}
		if((treasureVO.leastStep <= treasureVO.ticketCount + treasureVO.useNum && treasureVO.step.isEmpty()) || (treasure.isFirstUse() && treasure.useNum >= config.firstUseStep)){
			int x = treasureVO.gridX;
			int y = treasureVO.gridY;
			int bigRewardX = 0;
			int bigRewardY = 0;
			for(GridVO gridVO:treasureVO.map.values()){
				if(gridVO.isBigGift()){
					bigRewardX = gridVO.gridX;
					bigRewardY = gridVO.gridY;
				}
			}
			Map<MoveType,Integer> map = new HashMap<>();
			if(bigRewardX != x && bigRewardY == y){//只能左右方向移动
				if(x > bigRewardX){//保底在左
					map.put(MoveType.LEFT, x - bigRewardX);
				}else{//保底在右
					map.put(MoveType.RIGHT, bigRewardX - x);
				}
			}else if(bigRewardX == x && bigRewardY != y){//只能上下方向移动
				if(y > bigRewardY){//保底在上
					map.put(MoveType.UP, y - bigRewardY);
				}else{//保底在下
					map.put(MoveType.DOWN, bigRewardY - y);
				}
			}else if(bigRewardX != x && bigRewardY != y){//需要2步才能走到保底
				if(x > bigRewardX && y > bigRewardY){//保底需要向左走,再向上走
					map.put(MoveType.LEFT, x - bigRewardX);
					map.put(MoveType.UP, y - bigRewardY);
				}else if(x > bigRewardX && y < bigRewardY){//保底需要向左走,再向下走
					map.put(MoveType.LEFT, x - bigRewardX);
					map.put(MoveType.DOWN, bigRewardY - y);
				}else if(x < bigRewardX && y > bigRewardY){//保底需要向右走,再向上走
					map.put(MoveType.RIGHT, bigRewardX - x);
					map.put(MoveType.UP, y - bigRewardY);
				}else if(x < bigRewardX && y < bigRewardY){//保底需要向右走,再向下走
					map.put(MoveType.RIGHT, bigRewardX - x);
					map.put(MoveType.DOWN, bigRewardY - y);
				}
			}
			if(map.isEmpty() == false){
				Entry<MoveType, Integer> step = null;
				treasureVO.step = map;
				for(Entry<MoveType,Integer> entry : map.entrySet()){
					step = entry;
					break;
				}
				if(step != null){
					switch(step.getKey()){
					case DOWN:
						treasureVO.gridY += step.getValue();
						break;
					case LEFT:
						treasureVO.gridX -= step.getValue();
						break;
					case RIGHT:
						treasureVO.gridX += step.getValue();
						break;
					case UP:
						treasureVO.gridY -= step.getValue();
						break;
					default:
						break;
					}
					map.remove(step.getKey());
					return step;
				}
			}
		}
		return null;
	}
	
	private void sendExchangeReward(long actorId, RewardType rewardType,
			int id, int number, TreasureBattleResult treasureBattleResult) {
		long uuid = 0;
		switch (rewardType) {
		case GOODS:
			uuid = goodsFacade.addGoodsVO(actorId, GoodsAddType.MAZE_TREASURE_EXCHANGE, id, number).item;
			if (treasureBattleResult != null) {
				treasureBattleResult.awardGoods.put(uuid, number);
			}
			break;
		case EQUIP:
			uuid = equipFacade.addEquip(actorId, EquipAddType.MAZE_TREASURE_EXCHANGE, id).item;
			if (treasureBattleResult != null) {
				treasureBattleResult.equipList.add(uuid);
			}
			break;
		case HEROSOUL:
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.MAZE_TREASURE_EXCHANGE, id, number);
			if (treasureBattleResult != null) {
				treasureBattleResult.herosoulMap.put(id, number);
			}
			break;
		default:
			break;
		}
		
	}
}
