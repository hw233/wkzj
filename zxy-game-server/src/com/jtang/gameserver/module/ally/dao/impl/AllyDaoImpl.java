package com.jtang.gameserver.module.ally.dao.impl;

import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Ally;
import com.jtang.gameserver.module.ally.dao.AllyDao;

/**
 * 
 * @author pengzy
 * 
 */
@Repository
public class AllyDaoImpl implements AllyDao, CacheListener {	
	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;

	/**
	 * 角色对应的盟友缓存列表. 格式是: ConcurrentMap<actorId, Ally>
	 */
	private static ConcurrentMap<Long, Ally> ALLY_MAP = new ConcurrentLinkedHashMap.Builder<Long, Ally>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public Ally get(long actorId) {
		if (ALLY_MAP.containsKey(actorId)) {
			return ALLY_MAP.get(actorId);
		}
		Ally ally = jdbc.get(Ally.class, actorId);
		if (ally == null) {
			ally = Ally.valueOf(actorId);
		}
		ALLY_MAP.put(actorId, ally);
		return ally;
	}

	@Override
	public boolean update(Ally ally) {
		dbQueue.updateQueue(ally);
		return true;
	}

	@Override
	public int cleanCache(long actorId) {
		ALLY_MAP.remove(actorId);
		return ALLY_MAP.size();
	}

}
