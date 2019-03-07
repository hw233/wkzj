package com.jtang.gameserver.module.dailytask.facade.impl.update;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.StoryPassedEvent;
import com.jtang.gameserver.dataconfig.model.DailyTasksConfig;
import com.jtang.gameserver.dataconfig.service.DailyTaskService;
import com.jtang.gameserver.dbproxy.entity.DailyTask;
import com.jtang.gameserver.module.dailytask.facade.impl.BaseTaskUpdate;
import com.jtang.gameserver.module.dailytask.model.DailyTaskVO;
import com.jtang.gameserver.module.dailytask.type.DailyTaskType;

/**
 * 通关故事每日任务实现
 * @author ludd
 *
 */
@Component
public class StoryPassedTaskUpdateImpl extends BaseTaskUpdate{
	@PostConstruct
	private void init() {
		eventBus.register(EventKey.STORY_PASSED, this);
	}
	
	@Override
	protected DailyTaskType getDailyTaskType() {
		return DailyTaskType.STORY_PASSED;
	}
	
	@Override
	public void update(GameEvent event) {
		int vipLevel = vipFacade.getVipLevel(event.actorId);
		DailyTask task = dailyTaskDao.get(event.actorId, vipLevel);
		
		List<DailyTasksConfig> configs = DailyTaskService.getDailyTasksConfigByType(getDailyTaskType().getCode());
		if (configs == null) {
			return;
		}
		DailyTaskVO dailyTaskVO = null;
		DailyTasksConfig cfg = null;
		for (DailyTasksConfig dailyTasksConfig : configs) {
			DailyTaskVO vo = task.get(dailyTasksConfig.getTaskId());
			if (isComplete(vo, dailyTasksConfig)) {
				continue;
			} else {
				dailyTaskVO  = vo;
				cfg = dailyTasksConfig;
				break;
			}
		}
		if (dailyTaskVO == null) {
			return;
		}
		StoryPassedEvent storyPassedEvent = event.convert();
		int battleType = storyPassedEvent.battleType;
		if (battleType == 0) {// 合作关卡不算
			return;
		}
		int times = storyPassedEvent.times;
		updateProgress(event.actorId, task, dailyTaskVO, cfg, times);
		
	}
	
}
