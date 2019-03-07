package com.jtang.gameserver.module.dailytask.facade.impl.update;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.VipLevelChangeEvent;
import com.jtang.gameserver.dataconfig.model.DailyTasksConfig;
import com.jtang.gameserver.dataconfig.service.DailyTaskService;
import com.jtang.gameserver.dbproxy.entity.DailyTask;
import com.jtang.gameserver.module.dailytask.facade.impl.BaseTaskUpdate;
import com.jtang.gameserver.module.dailytask.helper.DailyTaskPushHelper;
import com.jtang.gameserver.module.dailytask.model.DailyTaskVO;
import com.jtang.gameserver.module.dailytask.type.DailyTaskType;

/**
 * vip任务实现
 * @author ludd
 *
 */
@Component
public class VIPLevelTaskUpdateImpl extends BaseTaskUpdate{
	@PostConstruct
	private void init() {
		eventBus.register(EventKey.VIP_LEVEL_CHANGE, this);
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
		VipLevelChangeEvent e = event.convert();
		updateProgress(event.actorId, task, dailyTaskVO, cfg, e.vipLevel);
		
	}
	
	@Override
	public boolean updateProgress(long actorId, DailyTask task, DailyTaskVO dailyTaskVO, DailyTasksConfig dailyTasksConfig, int progress) {
		if (dailyTaskVO.getProgress() < Integer.valueOf(dailyTasksConfig.getCompleteRule())) {
			dailyTaskVO.setProgress(progress);
			task.setProgressTime(TimeUtils.getNow());
			if (dailyTaskVO.getProgress() >= Integer.valueOf(dailyTasksConfig.getCompleteRule())) {
				dailyTaskVO.setComplte((byte) 1);
			}
			dailyTaskDao.update(task);
			DailyTaskPushHelper.pushTask(actorId, dailyTaskVO);
			return true;
		}
		return false;
	}
	
	@Override
	protected DailyTaskType getDailyTaskType() {
		return DailyTaskType.VIP_COMPLETE;
	}
	
}
