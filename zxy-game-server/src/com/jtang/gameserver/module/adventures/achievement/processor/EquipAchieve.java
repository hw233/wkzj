package com.jtang.gameserver.module.adventures.achievement.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.AddEquipEvent;
import com.jtang.gameserver.component.event.EquipEnhancedEvent;
import com.jtang.gameserver.component.event.EquipRefinedEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.dataconfig.model.AchievementConfig;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.service.AchievementService;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.module.adventures.achievement.helper.AchievePushHelper;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveAttributeKey;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;




/**
 * 装备相关的达成
 * @author 0x737263
 *
 */
@Component
public class EquipAchieve extends AbstractAchieve implements Receiver {
	
	@Override
	public void register() {
		eventBus.register(EventKey.ADD_EQUIP, this);
		eventBus.register(EventKey.EQUIP_REFINED, this);
		eventBus.register(EventKey.EQUIP_ENHANCED, this);
	}

	@Override
	public void onEvent(Event paramEvent) {

		if (paramEvent.getName() == EventKey.ADD_EQUIP) {
			AddEquipEvent addEquipEvent = (AddEquipEvent) paramEvent;
			EquipConfig equipConfig = EquipService.get(addEquipEvent.equipId);
			if (equipConfig.isWeapon()) {
				finishEquipRefineAchieve(addEquipEvent.actorId, AchieveType.WEAPON, equipConfig.getStar());
			}
			if (equipConfig.isArmor()) {
				finishEquipRefineAchieve(addEquipEvent.actorId, AchieveType.ARMOR, equipConfig.getStar());
			}
			if (equipConfig.isOrnaments()) {
				finishEquipRefineAchieve(addEquipEvent.actorId, AchieveType.DECORATION, equipConfig.getStar());
			}
		}

		if (paramEvent.getName() == EventKey.EQUIP_REFINED) {
			EquipRefinedEvent equipRefineEvent = (EquipRefinedEvent) paramEvent;
			EquipConfig equipConfig = EquipService.get(equipRefineEvent.equipId);
			for (int i = 0; i < equipRefineEvent.refineNum; i++) {
				finishEquipRefineAchieve(equipRefineEvent.actorId, AchieveType.EQUIP_REFINE, equipConfig.getStar());
				finishNumAchievement(equipRefineEvent.actorId, AchieveType.REFINE_NUM);
			}
		}

		if (paramEvent.getName() == EventKey.EQUIP_ENHANCED) {
			EquipEnhancedEvent enhancedEvent = (EquipEnhancedEvent) paramEvent;
			finishNumAchievement(enhancedEvent.actorId, AchieveType.EQUIP_ENHANCED_LEVEL, enhancedEvent.level);
		}
	}
	
	private void finishEquipRefineAchieve(long actorId, AchieveType achieveType, int star) {
		List<AchievementConfig> configList = AchievementService.getByType(achieveType.getId());
		if (configList == null) {
			return;
		}
		List<AchieveVO> list = new ArrayList<>();
		for (AchievementConfig config : configList) {
			List<Integer> conditionList = config.getConditionList();
			if (config.getCondition() == star) {
				continue;
			}
			
			AchieveVO achieveVO = achieveDao.getAchieveVO(actorId, config.getAchieveId(), config.getAchieveType());
			if (achieveVO == null) {
				if (conditionList.get(conditionList.size() - 1) == 1) {
					achieveVO = AchieveVO.valueOf(true, config.getAchieveId(), config.getConditionList());
				} else if (conditionList.get(conditionList.size() - 1) > 1) {
					achieveVO = AchieveVO.valueOf(false, config.getAchieveId(), config.getConditionList());
				} else {
					// LOGGER.error("achievement config story pass condition error, finish num is 0 or less than 0");
					continue;
				}
				achieveDao.addAchieveVO(actorId, achieveVO);
//				AchievePushHelper.pushAchievement(actorId, achieveVO);
			} else if(achieveVO.getConditionList() == null){
				continue;
			} else {
				if (achieveVO.isAchieved()) {
					continue;
				}
				Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
				int num = achieveVO.increaseFinishNum();
				if (num >= conditionList.get(conditionList.size() - 1)) {
					achieveVO.setFinished();
					changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achieveVO.getState());
					list.add(achieveVO);
				}

				achieveDao.update(actorId);

				changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
//				AchievePushHelper.pushAttribute(actorId, achieveVO.getAchieveId(), changedValues);
			}
		}
		AchievePushHelper.pushAchievement(actorId, list);
	}

}