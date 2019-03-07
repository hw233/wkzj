package com.jtang.gameserver.module.refine.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.EQUIP_NOT_FOUND;
import static com.jiatang.common.GameStatusCodeConstant.REFINE_GOODS_LEVEL_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.REFINE_NUMBER_MAX;
import static com.jiatang.common.GameStatusCodeConstant.REFINE_STONE_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.REFINE_STONE_OR_MONEY_NOT_ENOUGH;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.EquipType;
import com.jiatang.common.model.EquipVO;
import com.jtang.core.event.EventBus;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.component.event.EquipRefinedEvent;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.model.RefineConfig;
import com.jtang.gameserver.dataconfig.model.RefineEquipConfig;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dataconfig.service.RefineEquipService;
import com.jtang.gameserver.dataconfig.service.RefineService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAttributeKey;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.refine.constant.RefineRule;
import com.jtang.gameserver.module.refine.facade.RefineFacade;
import com.jtang.gameserver.module.refine.model.RefineResult;
import com.jtang.gameserver.module.refine.type.RefineType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;

@Component
public class RefineFacadeImpl implements RefineFacade {
	
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	EquipFacade equipFacade;
//	@Autowired
//	RefineDao refineDao;
	@Autowired
	GoodsFacade goodsFacade;
	@Autowired
	VipFacade vipFacade;
	@Autowired
	EventBus eventBus;
	@Autowired
	LineupFacade lineupFacade;
	
//	@Override
//	public Refine get(long actorId) {
//		return refineDao.get(actorId);
//	}

//	@Override
//	public TResult<Integer> upgrade(long actorId) {
//		Refine refine = get(actorId);
//		
//		int oldLevel = refine.level;
//		int nextLevel = oldLevel + 1;
//		
//		if (nextLevel > RefineService.maxLevel()) {
//			return TResult.valueOf(REFINE_LEVEL_CAP);
//		}
//		
//		RefineConfig roomCfg = RefineService.get(nextLevel);
//		boolean flag = vipFacade.decreaseTicket(actorId, TicketDecreaseType.REFINE_UPGRADE, roomCfg.getConsumeTicket(), oldLevel, nextLevel);
//		if (flag == false) {
//			return TResult.valueOf(TICKET_NOT_ENOUGH);
//		}
//		
//		refine.level = nextLevel;
//		refineDao.update(refine);
//		
//		Actor actor = actorFacade.getActor(actorId);
//		GameOssLogger.refineUpgrade(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, oldLevel, nextLevel);
//		
//		return TResult.sucess(nextLevel);
//	}

	@Override
	public TResult<RefineResult> refineEquip(long actorId, long uuid, int refineType , int refineNum) {
		if(refineNum < 1){
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		RefineType type = RefineType.getType(refineType);
		if(type == null){
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
//		Refine refine = get(actorId);
		EquipVO equip = equipFacade.get(actorId, uuid);
		if(equip == null){
			return TResult.valueOf(EQUIP_NOT_FOUND);
		}
		EquipConfig equipCfg = EquipService.get(equip.equipId);
		RefineEquipConfig refineEquipCfg = RefineEquipService.get(equipCfg.getStar(), equip.equipType.getId());
		RefineConfig refineConfig = RefineService.get(type);
		
		if(equip.refineNum + refineNum >= equip.maxRefineNum){
			refineNum = equip.maxRefineNum - equip.refineNum;
			if(refineNum < 1){
				return TResult.valueOf(REFINE_NUMBER_MAX);
			}
		}
				
		if(ableUseStone(equip) == false){
			return TResult.valueOf(REFINE_GOODS_LEVEL_NOT_ENOUGH);
		}
		Actor actor = actorFacade.getActor(actorId);
		TResult<Integer> refineResult = costResource(actor,refineNum,equip,type);//扣除金币和精炼石
		if(refineResult.isFail()){
			return TResult.valueOf(refineResult.statusCode);
		}
		refineNum = refineResult.item;

		Map<EquipAttributeKey, Number> attributeMaps = new HashMap<EquipAttributeKey, Number>();
		RefineResult result = new RefineResult();
		attributeMaps.put(EquipAttributeKey.REFINE_NUM, equip.refineNum + refineNum);
		result.refineNum = refineNum;
		int addVal = 0;
		int thisRefineValue = 0; //本次精炼净增加的值,用于oss
		for (int i = 0; i < refineNum; i++) {
			if(equip.equipType == EquipType.WEAPON){
				addVal = refineEquipCfg.getAttack(type.getCode());
				thisRefineValue += addVal + Math.round(addVal * refineConfig.getAddValue());
				equip.atk += addVal + Math.round(addVal * refineConfig.getAddValue());
				attributeMaps.put(EquipAttributeKey.ATK, equip.atk);
				result.key = EquipAttributeKey.ATK;
			}else if(equip.equipType == EquipType.ARMOR){
				addVal = refineEquipCfg.getDeffence(type.getCode());
				thisRefineValue += addVal + Math.round(addVal * refineConfig.getAddValue());
				equip.defense += addVal + Math.round(addVal * refineConfig.getAddValue());
				attributeMaps.put(EquipAttributeKey.DEFENSE, equip.defense);
				result.key = EquipAttributeKey.DEFENSE;
			}else if(equip.equipType == EquipType.ORNAMENTS){
				addVal = refineEquipCfg.getHp(type.getCode());
				thisRefineValue += addVal + Math.round(addVal * refineConfig.getAddValue());
				equip.hp += addVal + Math.round(addVal * refineConfig.getAddValue());
				attributeMaps.put(EquipAttributeKey.HP, equip.hp);
				result.key = EquipAttributeKey.HP;
			}
		}
		attributeMaps.put(EquipAttributeKey.COST_GOLD, equip.costGold);
		attributeMaps.put(EquipAttributeKey.COST_STONE, equip.costStoneNum);
		equipFacade.updateAttribute(actorId, uuid, attributeMaps);
		
		//重新计算阵型的buffer
		int gridIndex = lineupFacade.isEquipInLineup(actorId, uuid);
		if (gridIndex > 0) {
			lineupFacade.unassignEquip(actorId, uuid, false);
			lineupFacade.assignEquip(actorId, uuid, gridIndex);
		}
		
		result.addVal = thisRefineValue;
		eventBus.post(new EquipRefinedEvent(actorId, equip.equipId, refineNum));
		
		//oss 精炼后的值
		GameOssLogger.equipRefine(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, uuid, equip.equipId, equip.equipType,
				thisRefineValue, equip.refineNum, equip.maxRefineNum);
		
		return TResult.sucess(result);
	}
	
	/**
	 * 扣除金币和精炼石
	 * @param actor
	 * @param refineNum
	 * @return
	 */
	private TResult<Integer> costResource(Actor actor,int refineNum,EquipVO equip,RefineType type) {
		EquipConfig equipCfg = EquipService.get(equip.equipId);
		RefineEquipConfig refineEquipCfg = RefineEquipService.get(equipCfg.getStar(), equip.equipType.getId());
		int stoneNum = 0;//消耗精炼石数量
		int goldNum = 0;//消耗金币数量
		int goodsNum = goodsFacade.getCount(actor.getPkId(), RefineRule.REFINE_STONE);//拥有精炼石数量
		for (int i = 0; i < refineNum; i++) {//获取可以精炼的次数
			stoneNum += refineEquipCfg.getConsumeStone(type,equip.level, equip.refineNum + i);//需要精炼石数量
			goldNum += refineEquipCfg.getConsumeGold(equip.level, equipCfg.getStar(), equip.refineNum + i);//需要金币数量
			if (goodsNum < stoneNum || actor.gold < goldNum){//金币或者精炼石不够
				refineNum = i;
				stoneNum -= refineEquipCfg.getConsumeStone(type,equip.level, equip.refineNum + i);
				goldNum -= refineEquipCfg.getConsumeGold(equip.level, equipCfg.getStar(), equip.refineNum + i);
				break;
			}
		}
		if(goldNum > 0 && stoneNum > 0){
			actorFacade.decreaseGold(actor.getPkId(), GoldDecreaseType.REFINE, goldNum);//扣除金币
			goodsFacade.decreaseGoods(actor.getPkId(), GoodsDecreaseType.PROP_BASE, RefineRule.REFINE_STONE, stoneNum);//扣除精炼石
		}else if(goldNum > 0 && stoneNum == 0){
			actorFacade.decreaseGold(actor.getPkId(), GoldDecreaseType.REFINE, goldNum);//扣除金币
		}else{
			if(goldNum == 0){
				if(actor.gold < refineEquipCfg.getConsumeGold(equip.level, equipCfg.getStar(), equip.refineNum + 1)){
					return TResult.valueOf(REFINE_STONE_OR_MONEY_NOT_ENOUGH);
				}
			}
			if(stoneNum == 0){
				if(goodsNum < refineEquipCfg.getConsumeStone(type,equip.level, equip.refineNum + 1)){
					return TResult.valueOf(REFINE_STONE_NOT_ENOUGH);
				}
			}
		}
		equip.costGold += goldNum;
		equip.costStoneNum += stoneNum;
		return TResult.sucess(refineNum);
	}


	private boolean ableUseStone(EquipVO equip) {
		EquipConfig equipConfig = EquipService.get(equip.equipId);
		if (equip.level >= RefineRule.REFINE_USE_GOODS_MIN_LEVEL || equipConfig.getStar() >= RefineRule.REFINE_USE_GOODS_MIN_STAR) {
			return true;
		}
		return false;
	}

	@Override
	public void flushMaxRefineNum(long actorId, long uuid, int level) {
		EquipVO equip = equipFacade.get(actorId, uuid);
		EquipConfig equipCfg = EquipService.get(equip.equipId);
		RefineEquipConfig refineEquipCfg = RefineEquipService.get(equipCfg.getStar(), equip.equipType.getId());
		
		int num = refineEquipCfg.getRefineNum() * level;
		Map<EquipAttributeKey, Number> attributeMaps = new HashMap<EquipAttributeKey, Number>();
		attributeMaps.put(EquipAttributeKey.MAX_REFINE_NUM, num + equip.maxRefineNum);
		equipFacade.updateAttribute(actorId, uuid, attributeMaps);
	}
	
}
