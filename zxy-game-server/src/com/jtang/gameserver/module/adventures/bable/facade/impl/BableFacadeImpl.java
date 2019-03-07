package com.jtang.gameserver.module.adventures.bable.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.event.EventBus;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.BableSuccessEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.BableConfig;
import com.jtang.gameserver.dataconfig.model.BableExchangeConfig;
import com.jtang.gameserver.dataconfig.model.BableMonsterConfig;
import com.jtang.gameserver.dataconfig.model.BableSkipConfig;
import com.jtang.gameserver.dataconfig.service.BableService;
import com.jtang.gameserver.dataconfig.service.BableSkipService;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dataconfig.service.MonsterService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Bable;
import com.jtang.gameserver.dbproxy.entity.BableRecord;
import com.jtang.gameserver.module.adventures.bable.constant.BableRule;
import com.jtang.gameserver.module.adventures.bable.dao.BableDao;
import com.jtang.gameserver.module.adventures.bable.dao.BableRecordDao;
import com.jtang.gameserver.module.adventures.bable.facade.BableFacade;
import com.jtang.gameserver.module.adventures.bable.facade.BableRankFacade;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableAutoResponse;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableDataResponse;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableExcangeGoodsResponse;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableSkipResponse;
import com.jtang.gameserver.module.adventures.bable.helper.BablePushHelper;
import com.jtang.gameserver.module.adventures.bable.model.BableBattleResult;
import com.jtang.gameserver.module.adventures.bable.model.BableExchangeVO;
import com.jtang.gameserver.module.adventures.bable.model.BableHistoryVO;
import com.jtang.gameserver.module.adventures.bable.model.BableStateVO;
import com.jtang.gameserver.module.adventures.bable.type.BableState;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackMonsterRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;
@Component
public class BableFacadeImpl implements BableFacade, ActorLoginListener, ApplicationListener<ContextRefreshedEvent>,BattleCallBack,ZeroListener {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private BableDao bableDao;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private BattleFacade battleFacade;
	@Autowired
	private VipFacade vipFacade; 
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private Schedule schedule;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private EventBus eventBus;
	@Autowired
	private BableRecordDao bableRecordDao;
	
	@Autowired
	private BableRankFacade bableRankFacade;
	
	@Override
	public TResult<BableDataResponse> getBableInfo(long actorId,int bableType) {
		Bable bable = bableDao.get(actorId);
		int time = getLeadTime(bable);
		BableDataResponse response = new BableDataResponse(bable,getResetNum(actorId, 1),getReFightNum(actorId),bableType, time, bable.autoUseGoodsId);
		return TResult.sucess(response);
	}
	
	private int getLeadTime(Bable bable) {
		int time = -1;
		if (bable.autoTime > 0) {
			BableSkipConfig skipConfig = BableSkipService.get(bable.autoBableType, bable.autoUseGoodsId);
			
			time = (skipConfig.getEachFloorConsumeTime() * bable.autoFloor) - (TimeUtils.getNow() - bable.autoTime);
			time = time > 0 ? time : 0;
		}
		return time;
	}
	
	@Override
	public TResult<BableDataResponse> choiceBable(long actorId, int bableType) {
		Actor actor = actorFacade.getActor(actorId);
		BableConfig bableConfig = BableService.getBableConfig(bableType);
		if(bableConfig == null){
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		if(actor.level < bableConfig.greaterLevel){
			return TResult.valueOf(GameStatusCodeConstant.BABLE_ACTOR_LEVEL_NOT_ENOUGH);
		}
		Bable bable = bableDao.get(actorId);
		if(bable.bableStateVO != null){
			return TResult.valueOf(GameStatusCodeConstant.ACTOR_IN_BABLE);
		}
		if(bable.bableStateVO == null){
			BableStateVO bableStateVO = new BableStateVO();
			bableStateVO.type = bableConfig.bableId;
			bableStateVO.floor = 0;
			bableStateVO.star = 0;
			bableStateVO.useStar = 0;
			bableStateVO.useRetryNum = 0;
			bableStateVO.state = BableState.INT_BABLE.getState();
			bable.bableStateVO = bableStateVO;
			bable.exchangeMap = BableService.getExchangeList(bableConfig.bableId);
			bable.monsterMap = BableService.getMonsters(bableConfig.bableId, 1);
			bableDao.update(bable);
		}
		return getBableInfo(actorId,bable.bableStateVO.type);
	}
	
	@Override
	public Result startBattle(long actorId) {
		Actor actor = actorFacade.getActor(actorId);
		Bable bable = bableDao.get(actorId);
		if(bable.bableStateVO == null){
			return Result.valueOf(GameStatusCodeConstant.NO_CHOICE_BABLE);
		}
		if(bable.bableStateVO.state == BableState.BABLE_END.getState()){
			return Result.valueOf(GameStatusCodeConstant.BABLE_END);
		}
		BableStateVO bableStateVO = bable.bableStateVO;
		BableMonsterConfig monsterConfig = BableService.getBableMonsterConfig(bableStateVO.type,bableStateVO.floor + 1);
		BableConfig bableConfig = BableService.getBableConfig(bableStateVO.type);
		LineupFightModel atkLineup = LineupHelper.getLineupFight(actorId);//玩家阵容
		Map<Integer, Integer> monsters = bable.monsterMap;// 怪物配置
		Map<Integer, MonsterVO> monsterVOs = new HashMap<Integer, MonsterVO>();// 怪物阵容
		for (Integer key : monsters.keySet()) {
			monsterVOs.put(key, new MonsterVO(MonsterService.get(monsters.get(key))));
		}
		Map<Long, Map<AttackerAttributeKey, Integer>> defTeamAttrChange = new HashMap<>();// 怪物附加属性
		for (MonsterVO monsterVO : monsterVOs.values()) {
			Map<AttackerAttributeKey, Integer> attributeMap = new HashMap<>();
			int hp = FormulaHelper.executeCeilInt(monsterConfig.hpExpr, actor.level,bableStateVO.floor + 1);
			int attack = FormulaHelper.executeCeilInt(monsterConfig.attackExpr, actor.level,bableStateVO.floor + 1);
			int defense = FormulaHelper.executeCeilInt(monsterConfig.defenseExpr, actor.level,bableStateVO.floor + 1);
			attributeMap.put(AttackerAttributeKey.HP, hp * monsterVO.getHp());
			attributeMap.put(AttackerAttributeKey.ATK, attack * monsterVO.getAtk());
			attributeMap.put(AttackerAttributeKey.DEFENSE, defense * monsterVO.getDefense());
			defTeamAttrChange.put(monsterVO.getSpriteId(), attributeMap);
		}
		int actorMorale = actor.morale;// 玩家气势
		int mosterMorale = FormulaHelper.executeCeilInt(monsterConfig.morale, actor.level,bableStateVO.floor + 1);// 怪物气势
		AttackMonsterRequest event = new AttackMonsterRequest(EventKey.TREASURE_BATTLE, MapService.get(bableConfig.mapId), actorId, atkLineup,
				monsterVOs, actorMorale, mosterMorale, null, defTeamAttrChange, null, BattleType.BABLE);
		Result result = battleFacade.submitAtkMonsterRequest(event, this);
		if (result.isFail()) {
			return Result.valueOf(GameStatusCodeConstant.FIGHT_ERROR);
		}
		return Result.valueOf();
	}
	
	@Override
	public void execute(BattleResult result) {
		FightData fightData = result.fightData;
		long actorId = result.battleReq.actorId;
		Bable bable = bableDao.get(actorId);
		ChainLock lock = LockUtils.getLock(bable);
		BableStateVO bableStateVO = null;
		BableConfig bableConfig = null;
		BableBattleResult bableBattleResult = null;
		try{
			lock.lock();
			bableStateVO = bable.bableStateVO;
			bableConfig = BableService.getBableConfig(bableStateVO.type);
			if (fightData.result.isWin()) {// 胜利
				boolean isTopFloor = false;
				int extStar = 0;//额外奖励星数
				bableStateVO.floor ++;
				bableStateVO.star += bableConfig.getStar(fightData.result);
				if (checkVip(actorId)){// vip特权
					int vipLevel = vipFacade.getVipLevel(actorId);
					VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
					extStar = vipPrivilege.bableExtStar;
					bableStateVO.star += extStar;
				}
				if(bableStateVO.floor >= bableConfig.maxFloor){//是否登顶
					bableStateVO.state = BableState.BABLE_END.getState();
					bableStateVO.star += bableConfig.maxFloorExtraStarNum;
					extStar += bableConfig.maxFloorExtraStarNum;
					isTopFloor = true;
					//oss日志，止步该层
//					Actor actor = actorFacade.getActor(actorId);
//					GameOssLogger.bableBattle(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, bableConfig.bableId, bableConfig.maxFloor, bableStateVO.star, bableStateVO.star-bableStateVO.useStar);
				}
				BablePushHelper.pushBableStar(actorId, bableStateVO.star, bableStateVO.useStar);
				BableHistoryVO historyVO = bable.historyMap.get(bableStateVO.type);
				if(historyVO != null){
					if(bableStateVO.floor > historyVO.floor){//本次层数超过历史层数更新历史层数
						historyVO.floor = bableStateVO.floor;
						if(bableStateVO.star > historyVO.star){//本次星数超过历史星数更新历史星数
							historyVO.star = bableStateVO.star;
						}
					}
				}else{
					historyVO = new BableHistoryVO(bableStateVO.type,bableStateVO.floor,bableStateVO.star);
					bable.historyMap.put(bableStateVO.type, historyVO);
				}
				//本次是否有额外掉落
				RewardObject rewardObject = bableConfig.getReward(bableStateVO.floor);
				int goodsId = 0;
				int goodsNum = 0; 
				if(rewardObject != null){
					goodsId = rewardObject.id;
					goodsNum = rewardObject.num;
					actorFacade.addGold(actorId, GoldAddType.BABLE,goodsNum);
				}
				Map<Integer,Integer> map = new HashMap<>();
				if(isTopFloor == false){
					map = BableService.getMonsters(bableConfig.bableId, bableStateVO.floor + 1);//获取下一层怪物
					bable.monsterMap = map;
				}
				bableBattleResult = new BableBattleResult(fightData,bableStateVO.floor,bableConfig.getStar(fightData.result),goodsId,goodsNum,isTopFloor,extStar,map,bableStateVO.useRetryNum);
				eventBus.post(new BableSuccessEvent(actorId, bableConfig.bableId, bableStateVO.floor, 1));
			}else{
				bableBattleResult = new BableBattleResult(fightData,bableStateVO.floor,bableConfig.getStar(fightData.result),0,0,false,0,bable.monsterMap,bableStateVO.useRetryNum);
				if(bableStateVO.useRetryNum + 1 < getReFightNum(actorId)){
					bableStateVO.useRetryNum ++;
					bableDao.update(bable);
					bableBattleResult.useRefight = bableStateVO.useRetryNum;
					BablePushHelper.pushBableBattleResult(actorId, bableBattleResult);
					return;
				}else{
					bableStateVO.state = BableState.BABLE_END.getState();
					bableBattleResult.useRefight = bableStateVO.useRetryNum + 1;
//					Actor actor = actorFacade.getActor(actorId);
//					GameOssLogger.bableBattle(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, bableConfig.bableId, bableConfig.maxFloor, bableStateVO.star, bableStateVO.star-bableStateVO.useStar);
				}
			}
			record(actorId, bable.bableStateVO);
			bableDao.update(bable);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		BablePushHelper.pushBableBattleResult(actorId, bableBattleResult);
	}
	

	@Override
	public TResult<BableExcangeGoodsResponse> exchange(long actorId, int exchangeId, int exchangeNum) {
		Bable bable = bableDao.get(actorId);
		BableStateVO bableStateVO = bable.bableStateVO;
		if (bableStateVO == null) {
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		BableExchangeConfig exchangeConfig = BableService.getExchangeConfig(bableStateVO.type,exchangeId);
		if(exchangeConfig == null){
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		BableExchangeVO exchangeVO = bable.exchangeMap.get(exchangeId);
		if(exchangeVO == null){
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		if(exchangeVO.exchangeNum < exchangeNum){
			return TResult.valueOf(GameStatusCodeConstant.BABLE_EXCHANGE_GOODS_NOT_ENOUGH);
		}
		if(bableStateVO.star - bableStateVO.useStar < exchangeVO.useStar * exchangeNum){
			return TResult.valueOf(GameStatusCodeConstant.BABLE_STAR_NOT_ENOUGH);
		}
		bableStateVO.useStar += exchangeVO.useStar * exchangeNum;
		exchangeVO.exchangeNum -= exchangeVO.rewardNum * exchangeNum;
		bableDao.update(bable);
		BablePushHelper.pushBableStar(actorId, bableStateVO.star, bableStateVO.useStar);
		goodsFacade.addGoodsVO(actorId, GoodsAddType.BABLE_EXCHANGE, exchangeVO.rewardId,exchangeVO.rewardNum * exchangeNum);
		BableExcangeGoodsResponse response = new BableExcangeGoodsResponse(new ArrayList<>(bable.exchangeMap.values()));
		return TResult.sucess(response);
	}
	

	@Override
	public TResult<BableExcangeGoodsResponse> getExchangeList(long actorId) {
		Bable bable = bableDao.get(actorId);
		if(bable.bableStateVO == null){
			return TResult.valueOf(GameStatusCodeConstant.BABLE_MUST_EXCHANGE);
		}
		BableExcangeGoodsResponse response = new BableExcangeGoodsResponse(new ArrayList<>(bable.exchangeMap.values()));
		return TResult.sucess(response);
	}

	@Override
	public TResult<BableDataResponse> resetBable(long actorId) {
		Bable bable = bableDao.get(actorId);
		ChainLock lock = LockUtils.getLock(bable);
		try{
			lock.lock();
			if(bable.bableStateVO == null){
				return TResult.valueOf(GameStatusCodeConstant.BABLE_NOT_IN);
			}
			BableStateVO bableStateVO = bable.bableStateVO;
			if(bable.resetNum >= getResetNum(actorId,bableStateVO.type)){
				return TResult.valueOf(GameStatusCodeConstant.BABLE_RESET_NUM);
			}
			
			//记录本次登塔的oss
			Actor actor = actorFacade.getActor(actorId);
			GameOssLogger.bableBattle(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, bableStateVO.type, bableStateVO.floor, bableStateVO.star, bableStateVO.star-bableStateVO.useStar);
			
			bable.resetNum += 1;
			bable.bableStateVO = null;
			bable.resetTime = TimeUtils.getNow();
			bable.exchangeMap = new HashMap<>();
			bable.monsterMap = new HashMap<>();
			bableDao.update(bable);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return getBableInfo(actorId,0);
	}
	
	@Override
	public TResult<BableSkipResponse> skipFloorReward(long actorId) {
		Bable bable = bableDao.get(actorId);
		if (getLeadTime(bable) != 0) {
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		if(bable.bableStateVO == null){
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		if(bable.bableStateVO.state == BableState.BABLE_END.getState()){
			return TResult.valueOf(GameStatusCodeConstant.BABLE_END);
		}
		BableSkipResponse response = null;
		ChainLock lock = LockUtils.getLock(bable);
		try{
			lock.lock();
			BableConfig bableConfig = BableService.getBableConfig(bable.autoBableType);
			if(bableConfig == null){
				return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
			}
			BableStateVO bableStateVO = bable.bableStateVO;
			if(bableStateVO == null){
				bableStateVO = new BableStateVO();
			}
			if(bableStateVO.isSkip()){
				return TResult.valueOf(GameStatusCodeConstant.BABLE_SKIP_USER);
			}
			BableSkipConfig skipConfig = BableSkipService.get(bable.autoBableType, bable.autoUseGoodsId);
			if(skipConfig == null){
				return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
			}
			Actor actor = actorFacade.getActor(actorId);
			if(actor.level < skipConfig.getUseActorLevel()){
				return TResult.valueOf(GameStatusCodeConstant.BABLE_SKIP_ACTOR_LEVEL_ERROR);
			}
			BableHistoryVO historyVO = bable.getHostoryVO(bable.autoBableType);
//			if(historyVO == null){
//				return TResult.valueOf(GameStatusCodeConstant.BABLE_NOT_HISTORY);
//			}
//			if(historyVO.floor < skipConfig.getSkipYesterdayFloor()){
//				return TResult.valueOf(GameStatusCodeConstant.BABLE_SKIP_LAST_FLOOR_ERROR);
//			}
//			if(bableStateVO.floor > skipConfig.getSkipFloorNum(historyVO.floor)){
//				return TResult.valueOf(GameStatusCodeConstant.BABLE_SKIP_NOT_USE);
//			}
//			boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.BableSkip, skipConfig.getUseTicket(), 0, 0);
//			if(isOk == false){
//				return TResult.valueOf(StatusCode.TICKET_NOT_ENOUGH);
//			}
			int targetFloor = skipConfig.getSkipFloorNum(historyVO.floor);
			int addFloor = targetFloor - bableStateVO.floor;
			int extStar = 0;
			if (checkVip(actorId)) {// vip特权
				int vipLevel = vipFacade.getVipLevel(actorId);
				VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
				extStar = vipPrivilege.bableExtStar;
			}
			int targetStar = skipConfig.getRewardStarNum(bableStateVO.floor,targetFloor, extStar);
			int historyAddPercent = bableConfig.getHistoryFloorPercent(historyVO.floor);
			int targetGold = skipConfig.getRewardGoldNum(bableStateVO.floor,targetFloor, historyAddPercent);
			bableStateVO.type = bable.autoBableType;
			bableStateVO.floor = targetFloor;
			bableStateVO.star += targetStar;
			bableStateVO.setIsSkip();
			if(bableStateVO.floor > historyVO.floor){//本次层数超过历史层数更新历史层数
				historyVO.floor = bableStateVO.floor;
			}
			if(bableStateVO.star > historyVO.star){//本次星数超过历史星数更新历史星数
				historyVO.star = bableStateVO.star;
			}
			bableStateVO.state = BableState.INT_BABLE.getState();
			bable.bableStateVO = bableStateVO;
			bable.monsterMap = BableService.getMonsters(bableConfig.bableId, bableStateVO.floor + 1);
			if(bable.exchangeMap.isEmpty()){
				bable.exchangeMap = BableService.getExchangeList(bableConfig.bableId);
			}
			boolean isTopFloor = false;
			if(bableStateVO.floor >= bableConfig.maxFloor){//是否登顶
				bableStateVO.state = BableState.BABLE_END.getState();
				bableStateVO.star += bableConfig.maxFloorExtraStarNum;
				//extStar += bableConfig.maxFloorExtraStarNum;
				isTopFloor = true;
				//oss日志，止步该层
//				GameOssLogger.bableBattle(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, bableConfig.bableId, bableConfig.maxFloor, bableStateVO.star, bableStateVO.star-bableStateVO.useStar);
			}
			bable.autoTime = -1;
			
			bableDao.update(bable);
			BablePushHelper.pushBableStar(actorId, bable.bableStateVO.star, bable.bableStateVO.useStar);
			actorFacade.addGold(actorId, GoldAddType.BABLE, targetGold);
			extStar = extStar * addFloor;
			targetStar = targetStar - extStar;
			if(bableStateVO.state == BableState.BABLE_END.getState()){
				extStar = extStar + bableConfig.maxFloorExtraStarNum;
			}
			response = new BableSkipResponse(BableState.INT_BABLE.getState(),targetStar,extStar,10,targetGold,targetFloor,bable.monsterMap,isTopFloor);
			eventBus.post(new BableSuccessEvent(actorId, bableConfig.bableId, bableStateVO.floor, addFloor));
			record(actorId, bable.bableStateVO);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return TResult.sucess(response);
	}
	
	@Override
	public TResult<BableAutoResponse> skipFloor(long actorId, int bableType, int useGoodsId) {
		BableConfig bableConfig = BableService.getBableConfig(bableType);
		if(bableConfig == null){
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		Bable bable = bableDao.get(actorId);
		BableStateVO bableStateVO = bable.bableStateVO;
		if(bableStateVO == null){
			bableStateVO = new BableStateVO();
			bable.bableStateVO = bableStateVO;
		}
		if(bable.bableStateVO.state == BableState.BABLE_END.getState()){
			return TResult.valueOf(GameStatusCodeConstant.BABLE_END);
		}
		if(bableStateVO.isSkip()){
			return TResult.valueOf(GameStatusCodeConstant.BABLE_SKIP_USER);
		}
		BableSkipConfig skipConfig = BableSkipService.get(bableType, useGoodsId);
		if(skipConfig == null){
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		Actor actor = actorFacade.getActor(actorId);
		if(actor.level < skipConfig.getUseActorLevel()){
			return TResult.valueOf(GameStatusCodeConstant.BABLE_SKIP_ACTOR_LEVEL_ERROR);
		}
		BableHistoryVO historyVO = bable.getHostoryVO(bableType);
		if(historyVO == null){
			return TResult.valueOf(GameStatusCodeConstant.BABLE_NOT_HISTORY);
		}
		if(historyVO.floor < skipConfig.getSkipYesterdayFloor()){
			return TResult.valueOf(GameStatusCodeConstant.BABLE_SKIP_LAST_FLOOR_ERROR);
		}
		if(bableStateVO.floor >= skipConfig.getSkipFloorNum(historyVO.floor)){
			return TResult.valueOf(GameStatusCodeConstant.BABLE_SKIP_NOT_USE);
		}
		int targetFloor = skipConfig.getSkipFloorNum(historyVO.floor);
		int addFloor = targetFloor - bableStateVO.floor;
		int costTicket = (int) Math.ceil(skipConfig.getUseTicket() * addFloor);
		boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.BableSkip, costTicket, 0, 0);
		if(isOk == false){
			return TResult.valueOf(StatusCode.TICKET_NOT_ENOUGH);
		}
//		int num = goodsFacade.getCount(actorId, skipConfig.getConsumeGoodsId());
//		int consumeNum = (int) Math.floor(skipConfig.getEachFloorConsumeGoodsNum() * addFloor);
//		if (consumeNum > 0) {
//			if (num < consumeNum) {
//				return TResult.valueOf(GameStatusCodeConstant.BABLE_CONSUME_GOODS_NOT_ENOUGH);
//			}
//			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.BABLE, skipConfig.getConsumeGoodsId(), consumeNum);
//		}
		bable.autoTime = TimeUtils.getNow();
		bable.autoBableType = bableType;
		bable.autoFloor = addFloor;
		bable.autoUseGoodsId = useGoodsId;
		bableDao.update(bable);
		int time = getLeadTime(bable);
		BableAutoResponse bableAutoResponse = new BableAutoResponse((short)targetFloor,costTicket, time);
		return TResult.sucess(bableAutoResponse);
	}
	
	@Override
	public int getHasEnterTimes(long actorId) {
		Bable bable = bableDao.get(actorId);
		return bable.resetNum;
	}

	@Override
	public void onLogin(long actorId) {
		if (bableRankFacade.isStatistics()) {
			return;
		}
		Bable bable = bableDao.get(actorId);
		ChainLock lock = LockUtils.getLock(bable);
		try{
			lock.lock();
			if(DateUtils.isToday(bable.resetTime) == false){
				bable.resetNum = 0;
				bable.resetTime = TimeUtils.getNow();
				bableDao.update(bable);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}


	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		bableRankFacade.createRank();
	}
	
	@Override
	public void onZero() {
		// 创建新的排行榜并发送奖励
		bableRankFacade.createRank();
		bableRankFacade.sendReward();
		Set<Long> actorIds = playerSession.onlineActorList();
		for(long actorId:actorIds){
			Bable bable = bableDao.get(actorId);
			ChainLock lock = LockUtils.getLock(bable);
			try{
				lock.lock();
				bable.resetNum = 0;
				bable.resetTime = TimeUtils.getNow();
				bableDao.update(bable);
				BablePushHelper.pushBableReset(actorId);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}
	
	/**
	 * 获取红心次数
	 */
	public int getReFightNum(long actorId){
		return BableRule.BABLE_REFIGHT_NUM;
	}
	
	/**
	 * 判断是否是达到vip等级
	 * @param actorId
	 * @return
	 */
	private boolean checkVip(long actorId){
		int vipLevel = vipFacade.getVipLevel(actorId);
		if (vipLevel > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 登天塔重置次数
	 * @return
	 */
	private int getResetNum(long actorId,int bableType) {
		BableConfig bableConfig = BableService.getBableConfig(bableType);
//		if(checkVip(actorId)){
//			Vip2Privilege vip2Privilege = (Vip2Privilege) vipFacade.getVipPrivilege(Vip2Privilege.vipLevel);
//			return vip2Privilege.bableEnterTimesAdd + bableConfig.resetNum;
//			
//		}
		return bableConfig.resetNum;
	}
	
	private void record(long actorId, BableStateVO stateVO) {
		BableRecord bableRecord = bableRecordDao.get(actorId, stateVO.type);
		if (bableRecord.maxFloor < stateVO.floor) {
			bableRecord.maxFloor = stateVO.floor;
			bableRecord.maxStar = stateVO.star;
			bableRecord.useTime = System.currentTimeMillis();
			bableRecordDao.update(bableRecord);
		}
	}

}
