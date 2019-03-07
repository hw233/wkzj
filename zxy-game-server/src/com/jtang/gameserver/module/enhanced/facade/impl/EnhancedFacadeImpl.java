package com.jtang.gameserver.module.enhanced.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.EQUIP_NOT_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_UPGRADE_GOLD_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_UPGRADE_LIMIT;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.EquipVO;
import com.jtang.core.event.EventBus;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.gameserver.component.event.EquipEnhancedEvent;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.ActorUpgradeConfig;
import com.jtang.gameserver.dataconfig.model.EnhancedConfig;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.model.EquipUpgradeConfig;
import com.jtang.gameserver.dataconfig.service.ActorService;
import com.jtang.gameserver.dataconfig.service.EnhancedService;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.ally.facade.AllyFacade;
import com.jtang.gameserver.module.enhanced.facade.EnhancedFacade;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAttributeKey;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.refine.facade.RefineFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;

@Component
public class EnhancedFacadeImpl implements EnhancedFacade {
	private static final Logger LOGGER = LoggerFactory.getLogger(EnhancedFacadeImpl.class);
	@Autowired
	private AllyFacade allyFacade;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private LineupFacade lineupFacade;
	@Autowired
	private RefineFacade refineFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private EventBus eventBus;
//	@Override
//	public Enhanced get(long actorId) {
//		return enhancedDao.get(actorId);
//	}
//
//	@Override
//	public TResult<Integer> upgrade(long actorId) {
//		Enhanced enhanced = get(actorId);
//		if (enhanced == null) {
//			return TResult.valueOf(ENHANCED_NOT_EXISTS);
//		}
//		int oldLevel = enhanced.level;
//		int nextLevel = enhanced.level + 1;
//		// 达到了强化室的等级上限
//		if (nextLevel > EnhancedService.getMaxLevel()) {
//			return TResult.valueOf(ENHANCED_LEVEL_LIMIT);
//		}
//		
//		EnhancedConfig enhancedConfig = EnhancedService.get(nextLevel);
//		if (enhancedConfig == null) {
//			return TResult.valueOf(ENHANCED_UPGRADE_FAIL);
//		}
//		
//		// 需要盟友等级总和达到一定的值
//		int needAllyLevel = enhancedConfig.getAllyLevel();
//		int allyLevel = allyFacade.getAllyLevel(actorId);
//		
//		if (needAllyLevel > 0 && allyLevel < needAllyLevel) {
//			return TResult.valueOf(ALLY_LEVEL_SUM_NOT_ENOUGH);
//		}
//		
//		// 扣除点券
//		int needTicket = enhancedConfig.getUpgradeTicket();
//		if (needTicket > 0 && !vipFacade.decreaseTicket(actorId, TicketDecreaseType.ENHANCED_UPGRADE, needTicket, oldLevel, nextLevel)) {
//			return TResult.valueOf(TICKET_NOT_ENOUGH);
//		}
//
//		// 强化室升级
//		if (!enhancedDao.update(actorId, nextLevel)) {
//			return TResult.valueOf(ENHANCED_UPGRADE_FAIL);
//		}
//		
//		Actor actor = actorFacade.getActor(actorId);
//		GameOssLogger.enhancedUpgrade(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, oldLevel, nextLevel);
//		
//		return TResult.sucess(nextLevel);
//	}

	@Override
	public Result enhanceEquip(long actorId, long equipUuid, int upgradeNum) {
		EquipVO equipVO = equipFacade.get(actorId, equipUuid);
		if (equipVO == null) {
			return Result.valueOf(EQUIP_NOT_EXISTS);
		}
//		Enhanced enhanced = get(actorId);
//		if (enhanced == null) {
//			return Result.valueOf(ENHANCED_NOT_EXISTS);
//		}

		Actor actor = actorFacade.getActor(actorId);
		ActorUpgradeConfig actorUpConfig = ActorService.getUpgradeConfig(actor.level);
		int equipLvLimit = actorUpConfig.getEquipLevelLimit();

		if (equipVO.level >= equipLvLimit) {
			return Result.valueOf(EQUIP_UPGRADE_LIMIT);
		}
		if(equipVO.level + upgradeNum > actorUpConfig.getEquipLevelLimit()){
			upgradeNum = actorUpConfig.getEquipLevelLimit() - equipVO.level;
		}
		// 消耗强化需要的金币
		EquipConfig config = EquipService.get(equipVO.equipId);
		EquipUpgradeConfig upgradeConfig = EquipService.getUpgradeConfig(config.getStar(), config.getType());
		// x1:前装备类型为/x2:装备等级为x2/x3:装备品质为
		int needGold = 0;
		for (int i = 0; i < upgradeNum; i++) {
			needGold += upgradeConfig.getNeedGolds(config.getType(), equipVO.level + i, config.getStar());
			if(actor.gold < needGold){
				upgradeNum = i;
				needGold -= upgradeConfig.getNeedGolds(config.getType(), equipVO.level + i, config.getStar());
			}
		}
		boolean flag = actorFacade.decreaseGold(actorId, GoldDecreaseType.ENHANCED, needGold);
		if (flag == false) {
			return Result.valueOf(EQUIP_UPGRADE_GOLD_NOT_ENOUGH);
		}
		//记录强化和精炼累计消耗的金币
		equipVO.costGold += needGold;
		
		// 根据强化室的等级,有一定的概率连升数级
//		int upLevel = 1;
		EnhancedConfig enhancedConfig = EnhancedService.get();
//		if (enhancedConfig.getProbability() > 0) {
//			int probability = enhancedConfig.getProbability();
//			int number = RandomUtils.nextInt(0, 100);
//			if (probability > number) {
//				if (equipVO.level + enhancedConfig.getUpLevel() > equipLvLimit) {
//					upLevel = equipLvLimit - equipVO.level;
//				} else {
//					upLevel = enhancedConfig.getUpLevel();
//				}
//			}
//		}

		int oldLevel = equipVO.level;
		int newLevel = upgradeNum + equipVO.level;
		// 强化获得的属性总值
		int enhancedAttributeValue = (int) Math.ceil((enhancedConfig.getAddValue() + 1) * upgradeConfig.getAttributeValue()) * (newLevel - 1);
		
		// 本次强化净增加的值 oss用
		int thisEnhancedValue = enhancedAttributeValue - equipVO.enhancedAttributeValue;

		upgradeEquip(actorId, equipVO, upgradeNum, enhancedAttributeValue);
		int gridIndex = lineupFacade.isEquipInLineup(actorId, equipVO.uuid);
		if (gridIndex > 0) {
			lineupFacade.unassignEquip(actorId, equipVO.uuid, false);
			lineupFacade.assignEquip(actorId, equipVO.uuid, gridIndex);
		}
		
		eventBus.post(new EquipEnhancedEvent(actorId, equipVO.equipId, equipVO.level,upgradeNum));
		
		refineFacade.flushMaxRefineNum(actorId, equipVO.uuid, upgradeNum);
		
		//OSS
		GameOssLogger.equipEnhanced(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, equipVO.uuid, equipVO.equipId,
				config.getEquipType(), thisEnhancedValue, oldLevel, newLevel);
		
		return Result.valueOf();
	}

	private int upgradeEquip(long actorId, EquipVO equipVO, int addLevel, int enhancedAttributeValue) {
		Map<EquipAttributeKey, Number> attributeMaps = new HashMap<>();
		attributeMaps.put(EquipAttributeKey.LEVEL, equipVO.level + addLevel);
		attributeMaps.put(EquipAttributeKey.ENHANCED_NUM, equipVO.enhancedNum + 1);// 可能没有此需求
		
		int addValue= 0; //增加的值
		switch (equipVO.equipType) {
		case WEAPON:
			if (equipVO.enhancedAttributeValue >= 0) {
				addValue = equipVO.atk - equipVO.enhancedAttributeValue + enhancedAttributeValue;
				attributeMaps.put(EquipAttributeKey.ATK, addValue);// 加攻
			}
			break;
		case ARMOR:
			if (equipVO.enhancedAttributeValue >= 0) {
				addValue = equipVO.defense - equipVO.enhancedAttributeValue + enhancedAttributeValue;
				attributeMaps.put(EquipAttributeKey.DEFENSE, addValue);// 加防
			}
			break;
		case ORNAMENTS:
			if (equipVO.enhancedAttributeValue >= 0) {
				addValue = equipVO.hp - equipVO.enhancedAttributeValue + enhancedAttributeValue;
				attributeMaps.put(EquipAttributeKey.HP, addValue);// 加HP
			}
			break;
		default:
			break;
		}
		
		equipVO.enhancedAttributeValue = enhancedAttributeValue;
		attributeMaps.put(EquipAttributeKey.COST_GOLD, equipVO.costGold);
		short code = equipFacade.updateAttribute(actorId, equipVO.uuid, attributeMaps);
		if(code != StatusCode.SUCCESS) {
			LOGGER.warn(String.format("update attribute error actorId:[%s] uuid:[%s] statuscode:[%s]", actorId, equipVO.uuid, code));
		}
		return addValue;
	}


}
