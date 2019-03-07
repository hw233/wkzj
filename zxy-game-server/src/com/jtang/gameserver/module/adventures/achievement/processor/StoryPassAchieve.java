package com.jtang.gameserver.module.adventures.achievement.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.StoryPassedEvent;
import com.jtang.gameserver.dataconfig.model.AchievementConfig;
import com.jtang.gameserver.dataconfig.service.AchievementService;
import com.jtang.gameserver.module.adventures.achievement.helper.AchievePushHelper;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveAttributeKey;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

@Component
public class StoryPassAchieve extends AbstractAchieve implements Receiver {

	@Override
	public void register() {
		eventBus.register(EventKey.STORY_PASSED, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		finishStoryPassIfAble((StoryPassedEvent) paramEvent);
	}
	
	private void finishStoryPassIfAble(StoryPassedEvent event) {
		List<AchievementConfig> configList = AchievementService.getByType(AchieveType.STORY_PASSED.getId());
		if (configList == null) {
			return;
		}
		List<AchieveVO> list = new ArrayList<>();
		for (AchievementConfig config : configList) {
			List<Integer> conditionList = config.getConditionList();
			if (event.battleId != conditionList.get(0) || event.star < conditionList.get(1)) {
				continue;
			}

			AchieveVO achieveVO = achieveDao.getAchieveVO(event.actorId, config.getAchieveId(), config.getAchieveType());
			if (achieveVO == null) {
				if (conditionList.get(2) == 1) {
					achieveVO = AchieveVO.valueOf(true, config.getAchieveId(), config.getConditionList());
					list.add(achieveVO);
				} else if (conditionList.get(2) > 1) {
					achieveVO = AchieveVO.valueOf(false, config.getAchieveId(), config.getConditionList());
				} else {
					// LOGGER.error("achievement config story pass condition error, finish num is 0 or less than 0");
					continue;
				}
				achieveDao.addAchieveVO(event.actorId, achieveVO);
//				AchievePushHelper.pushAchievement(event.actorId, achieveVO);
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
				achieveDao.update(event.actorId);
				changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
//				AchievePushHelper.pushAttribute(event.actorId, achieveVO.getAchieveId(), changedValues);
			}
		}
		AchievePushHelper.pushAchievement(event.actorId, list);
	}
}