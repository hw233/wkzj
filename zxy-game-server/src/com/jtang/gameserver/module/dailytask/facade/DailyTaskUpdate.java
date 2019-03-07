package com.jtang.gameserver.module.dailytask.facade;

import com.jtang.core.event.GameEvent;
import com.jtang.gameserver.dataconfig.model.DailyTasksConfig;
import com.jtang.gameserver.dbproxy.entity.DailyTask;
import com.jtang.gameserver.module.dailytask.model.DailyTaskVO;

public interface DailyTaskUpdate {
	/**
	 * 更新
	 * @param event
	 */
	public void update(GameEvent event);
	
	/**
	 * 是否已完成
	 * @param dailyTaskVO
	 * @param dailyTasksConfig
	 * @return
	 */
	public boolean isComplete(DailyTaskVO dailyTaskVO, DailyTasksConfig dailyTasksConfig);
	
	/**
	 * 更新进度
	 * @param actorId
	 * @param dailyTaskVO
	 * @param dailyTasksConfig
	 * @return
	 */
	public boolean updateProgress(long actorId, DailyTask task,DailyTaskVO dailyTaskVO, DailyTasksConfig dailyTasksConfig);
	/**
	 * 更新进度
	 * @param actorId
	 * @param dailyTaskVO
	 * @param dailyTasksConfig
	 * @return
	 */
	public boolean updateProgress(long actorId, DailyTask task,DailyTaskVO dailyTaskVO, DailyTasksConfig dailyTasksConfig, int progress);
}
