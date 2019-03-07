package com.jtang.gameserver.module.adventures.achievement.processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.NameChangeEvent;
import com.jtang.gameserver.dataconfig.model.AchievementConfig;
import com.jtang.gameserver.dataconfig.service.AchievementService;
import com.jtang.gameserver.module.adventures.achievement.helper.AchievePushHelper;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

@Component
public class NameChangeAchieve extends AbstractAchieve implements Receiver {
	
	@Override
	public void register() {
		eventBus.register(EventKey.NAME_CHANGED, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		NameChangeEvent event = (NameChangeEvent) paramEvent;
		finishAchievementIfAble(event.actorId, AchieveType.NAME_CHANGE.getId());
	}
	
	private void finishAchievementIfAble(long actorId, int achieveType) {
		List<AchievementConfig> configList = AchievementService.getByType(achieveType);
		if (configList == null) {
			return;
		}
		List<AchieveVO> list = new ArrayList<>();
		AchievementConfig config = configList.get(0);
		AchieveVO achieveVO = achieveDao.getAchieveVO(actorId, config.getAchieveId(), config.getAchieveType());
		if (achieveVO == null) {
			achieveVO = AchieveVO.valueOf(config.getAchieveId(), config.getCondition());
			achieveVO.setFinished();
			achieveDao.addAchieveVO(actorId, achieveVO);
			list.add(achieveVO);
			AchievePushHelper.pushAchievement(actorId, list);
		}
	}
}
