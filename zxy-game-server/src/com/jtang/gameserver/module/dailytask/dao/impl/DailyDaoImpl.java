package com.jtang.gameserver.module.dailytask.dao.impl;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dataconfig.service.DailyTaskService;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.DailyTask;
import com.jtang.gameserver.module.dailytask.dao.DailyTaskDao;
@Component
public class DailyDaoImpl implements DailyTaskDao, CacheListener {

	@Autowired
	private IdTableJdbc jdbc;
	
	@Autowired
	private DBQueue dbQueue;
	private static ConcurrentMap<Long, DailyTask> TASKS = new ConcurrentLinkedHashMap.Builder<Long, DailyTask>().maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	@Override
	public DailyTask get(long actorId, int vipLevel) {
		if (TASKS.containsKey(actorId)) {
			return TASKS.get(actorId);
		}
		DailyTask tasks = jdbc.get(DailyTask.class, actorId);
		if (tasks == null) {
			tasks = DailyTask.valueOf(actorId);
		}
		List<Integer> list = DailyTaskService.getTaskIds();
		for (int taskId : list) {
			tasks.initVO(taskId, vipLevel);
		}
		TASKS.put(actorId, tasks);
		return tasks;
	}

	@Override
	public void update(DailyTask dailyTask) {
		dbQueue.updateQueue(dailyTask);
	}

	@Override
	public int cleanCache(long actorId) {
		TASKS.remove(actorId);
		return TASKS.size();
	}

}
