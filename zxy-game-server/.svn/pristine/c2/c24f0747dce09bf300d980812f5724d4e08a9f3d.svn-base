package com.jtang.gameserver.module.dailytask.facade.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.GameEvent;
import com.jtang.core.event.Receiver;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.DailyTasksConfig;
import com.jtang.gameserver.dataconfig.service.DailyTaskService;
import com.jtang.gameserver.dbproxy.entity.DailyTask;
import com.jtang.gameserver.module.dailytask.dao.DailyTaskDao;
import com.jtang.gameserver.module.dailytask.facade.DailyTaskUpdate;
import com.jtang.gameserver.module.dailytask.helper.DailyTaskPushHelper;
import com.jtang.gameserver.module.dailytask.model.DailyTaskVO;
import com.jtang.gameserver.module.dailytask.type.DailyTaskType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;

/**
 * 每日任务进度更新基类
 * @author ludd
 *
 */
@Component
public abstract class BaseTaskUpdate implements DailyTaskUpdate, Receiver{
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseTaskUpdate.class);
	
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	protected DailyTaskDao dailyTaskDao;
	@Autowired
	protected EventBus eventBus;
	
	@Autowired
	protected VipFacade vipFacade;
	
	@Override
	public void onEvent(Event paramEvent) {
		GameEvent event = paramEvent.convert();
		int vipLevel = vipFacade.getVipLevel(event.actorId);
		DailyTask task = dailyTaskDao.get(event.actorId, vipLevel);
		if (DateUtils.isToday(task.getProgressTime()) == false) {
			task.clear(vipLevel);
		}
		update(event);
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
		ChainLock lock = LockUtils.getLock(dailyTaskVO);
		try {
			lock.lock();
			updateProgress(event.actorId, task, dailyTaskVO, cfg);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		
	}
	@Override
	public boolean isComplete(DailyTaskVO dailyTaskVO, DailyTasksConfig dailyTasksConfig) {
		if (dailyTaskVO.getIsGet() == 1) {
			return true;
		}
		if (dailyTaskVO.getProgress() >= Integer.valueOf(dailyTasksConfig.getCompleteRule())) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean updateProgress(long actorId, DailyTask task, DailyTaskVO dailyTaskVO, DailyTasksConfig dailyTasksConfig) {
		return updateProgress(actorId, task, dailyTaskVO, dailyTasksConfig, 1);
	}
	
	@Override
	public boolean updateProgress(long actorId, DailyTask task, DailyTaskVO dailyTaskVO, DailyTasksConfig dailyTasksConfig, int progress) {
		if (dailyTaskVO.getProgress() < Integer.valueOf(dailyTasksConfig.getCompleteRule())) {
			dailyTaskVO.setProgress(dailyTaskVO.getProgress() + progress);
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
	
	protected abstract DailyTaskType getDailyTaskType();
	
}
