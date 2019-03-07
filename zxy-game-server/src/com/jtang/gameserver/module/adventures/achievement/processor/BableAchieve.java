package com.jtang.gameserver.module.adventures.achievement.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.BableSuccessEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.dataconfig.model.AchievementConfig;
import com.jtang.gameserver.dataconfig.service.AchievementService;
import com.jtang.gameserver.module.adventures.achievement.helper.AchievePushHelper;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveAttributeKey;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

@Component
public class BableAchieve extends AbstractAchieve implements Receiver {

	@Override
	public void register() {
		eventBus.register(EventKey.BABLE_SUCESS, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		BableSuccessEvent event = (BableSuccessEvent) paramEvent;
		finishNumAchievement(event.actorId, AchieveType.BABLE_FLOOR, event.floor);
		
		finishBableDifficultyAchieve(event.actorId, AchieveType.BABLE_DIFFICULTY_FLOOR, event.bableId, event.floor);
	}
	
	private void finishBableDifficultyAchieve(long actorId, AchieveType achieveType, int bableId, int floor) {
		List<AchievementConfig> configList = AchievementService.getByType(AchieveType.BABLE_DIFFICULTY_FLOOR.getId());
		if (configList == null) {
			return;
		}
		List<AchieveVO> list = new ArrayList<>();
		for (AchievementConfig config : configList) {
			List<Integer> conditionList = config.getConditionList();
			if (bableId != conditionList.get(0)) {
				continue;
			}

			AchieveVO achieveVO = achieveDao.getAchieveVO(actorId, config.getAchieveId(), config.getAchieveType());
			if (achieveVO == null) {
				achieveVO = AchieveVO.valueOf(config.getAchieveId(), bableId, floor);

				if (floor >= conditionList.get(1)) {
					achieveVO.setFinished();
					list.add(achieveVO);
				}

				achieveDao.addAchieveVO(actorId, achieveVO);
//				AchievePushHelper.pushAchievement(actorId, achieveVO);
			}else if(achieveVO.getConditionList() == null){
				continue;
			} else {
				if (achieveVO.isAchieved()) {
					continue;
				}

				// 累加
				Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
				if (floor >= conditionList.get(1)) {// 完成
					achieveVO.setCondition2(floor);
					achieveVO.setFinished();
					changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achieveVO.getState());
					changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
					list.add(achieveVO);
				} else {
					achieveVO.setCondition2(floor);
					changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
				}
				achieveDao.update(actorId);
//				AchievePushHelper.pushAttribute(actorId, achieveVO.getAchieveId(), changedValues);
			}
		}
		AchievePushHelper.pushAchievement(actorId, list);
	}	
}