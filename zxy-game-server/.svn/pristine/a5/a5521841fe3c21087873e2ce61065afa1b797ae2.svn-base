package com.jtang.gameserver.module.adventures.bable.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Bable;
import com.jtang.gameserver.module.adventures.bable.dao.BableDao;
@Component
public class BableDaoImpl implements BableDao, CacheListener {

	@Autowired
	private IdTableJdbc jdbcTemplate;
	@Autowired
	private DBQueue dbQueue;
	
	private ConcurrentLinkedHashMap<Long, Bable> BABLE_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Bable>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public Bable get(long actorId) {
		if (BABLE_MAPS.containsKey(actorId)) {
			return BABLE_MAPS.get(actorId);
		}
		Bable bable = jdbcTemplate.get(Bable.class, actorId);
		if (bable == null) {
			bable = Bable.valueOf(actorId);
		}
		BABLE_MAPS.put(actorId, bable);
		return bable;
	}

	@Override
	public boolean update(Bable bable) {
		queueUpdate(bable);
		return true;
	}
	
	private void queueUpdate(Bable entity) {
		dbQueue.updateQueue(entity);
	}


	@Override
	public int cleanCache(long actorId) {
		BABLE_MAPS.remove(actorId);
		return BABLE_MAPS.size();
	}
	

}
