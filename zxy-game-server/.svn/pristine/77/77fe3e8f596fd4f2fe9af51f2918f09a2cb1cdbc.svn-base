package com.jtang.gameserver.module.hole.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.HoleTrigger;
import com.jtang.gameserver.module.hole.dao.HoleTriggerDao;

@Component
public class HoleTriggerDaoImpl implements HoleTriggerDao, CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;

	private static ConcurrentLinkedHashMap<Long, HoleTrigger> HOLE_TRIGGER_MAP = new ConcurrentLinkedHashMap.Builder<Long, HoleTrigger>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public HoleTrigger get(long actorId) {
		if (HOLE_TRIGGER_MAP.containsKey(actorId)) {
			return HOLE_TRIGGER_MAP.get(actorId);
		}
		HoleTrigger holeTrigger = jdbc.get(HoleTrigger.class, actorId);
		if (holeTrigger == null) {
			holeTrigger = HoleTrigger.valueOf(actorId);
		}
		HOLE_TRIGGER_MAP.put(actorId, holeTrigger);
		return holeTrigger;
	}

	@Override
	public void update(HoleTrigger holeTrigger) {
		holeTrigger.operationTime = TimeUtils.getNow();
		dbQueue.updateQueue(holeTrigger);
	}

	@Override
	public int cleanCache(long actorId) {
		HOLE_TRIGGER_MAP.remove(actorId);
		return HOLE_TRIGGER_MAP.size();
	}
	
	

}
