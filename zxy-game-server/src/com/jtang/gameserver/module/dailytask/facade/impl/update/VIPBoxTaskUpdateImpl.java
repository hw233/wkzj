package com.jtang.gameserver.module.dailytask.facade.impl.update;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.VIPBoxTaskEvent;
import com.jtang.gameserver.dataconfig.model.DailyTasksConfig;
import com.jtang.gameserver.dataconfig.service.DailyTaskService;
import com.jtang.gameserver.dbproxy.entity.DailyTask;
import com.jtang.gameserver.module.dailytask.facade.impl.BaseTaskUpdate;
import com.jtang.gameserver.module.dailytask.helper.DailyTaskPushHelper;
import com.jtang.gameserver.module.dailytask.model.DailyTaskVO;
import com.jtang.gameserver.module.dailytask.type.DailyTaskType;

/**
 * 福神宝箱每日任务实现
 * @author hezh
 *
 */
@Component
public class VIPBoxTaskUpdateImpl extends BaseTaskUpdate{
	@PostConstruct
	private void init() {
		eventBus.register(EventKey.OPEN_VIP_BOX, this);
	}
	
	@Override
	protected DailyTaskType getDailyTaskType() {
		return DailyTaskType.VIP_BOX;
	}
	
	@Override
	public void update(GameEvent event) {
		int vipLevel = vipFacade.getVipLevel(event.actorId);
		DailyTask task = dailyTaskDao.get(event.actorId, vipLevel);
		List<DailyTasksConfig> configs = DailyTaskService.getDailyTasksConfigByType(getDailyTaskType().getCode());
		if (configs == null) {
			return;
		}
		VIPBoxTaskEvent vIPBoxTaskEvent = (VIPBoxTaskEvent)event;
		for (DailyTasksConfig dailyTasksConfig : configs) {
			DailyTaskVO vo = task.get(dailyTasksConfig.getTaskId());
			if (!isComplete(vo, dailyTasksConfig)) {
				//没完成的开启福神宝箱任务的进度都+1
				updateProgress(event.actorId, task, vo, dailyTasksConfig,vIPBoxTaskEvent.getOpenNum());
			}
		}
	}
	
	@Override
	public boolean updateProgress(long actorId, DailyTask task, DailyTaskVO dailyTaskVO, DailyTasksConfig dailyTasksConfig,int progress) {
		int totalTimes =  Integer.valueOf(dailyTasksConfig.getCompleteRule());
		int currProgress = dailyTaskVO.getProgress();
		if (currProgress < totalTimes) {
			dailyTaskVO.setProgress(currProgress + progress);
			task.setProgressTime(TimeUtils.getNow());
			if (dailyTaskVO.getProgress() >= totalTimes) {
				dailyTaskVO.setComplte((byte) 1);
			}
			dailyTaskDao.update(task);
			DailyTaskPushHelper.pushTask(actorId, dailyTaskVO);
			return true;
		}
		return false;
	}
}
