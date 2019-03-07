package com.jtang.gameserver.module.snatch.dao.impl;

import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Snatch;
import com.jtang.gameserver.module.snatch.dao.SnatchDao;

/**
 * 抢夺接口实现
 * 因为有全局排名。不能继承CacheListener 删除snatch实体
 * @author liujian
 *
 */
@Component
public class SnatchDaoImpl implements SnatchDao {

	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	
	/**
	 * 抢夺数据缓存列表
	 * 格式是:
	 * ConcurrentMap<actorId, Snatch>
	 */
	private static ConcurrentMap<Long, Snatch> SNATCHS = new ConcurrentLinkedHashMap.Builder<Long, Snatch>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();

	
	@Override
	public Snatch get(long actorId) {
		if (SNATCHS.containsKey(actorId)) {
			return SNATCHS.get(actorId);
		}
		Snatch snatch = jdbc.get(Snatch.class, actorId);
		if (snatch == null) {
			snatch = Snatch.valueOf(actorId);
		}
		SNATCHS.put(actorId, snatch);
		return snatch;
	}

	@Override
	public boolean update(Snatch snatch) {
		dbQueue.updateQueue(snatch);
		return true;
	}
}
