package com.jtang.gameserver.module.adventures.favor.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Favor;
import com.jtang.gameserver.module.adventures.favor.dao.FavorDao;

@Repository
public class FavorDaoImpl implements FavorDao, CacheListener {	
	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, Favor> FAVOR_MAP = new ConcurrentLinkedHashMap.Builder<Long, Favor>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Favor get(long actorId) {
		if (FAVOR_MAP.containsKey(actorId)) {
			return FAVOR_MAP.get(actorId);
		}

		Favor favor = jdbc.get(Favor.class, actorId);
		if (favor == null) {
			favor = Favor.valueOf(actorId);
		}
		FAVOR_MAP.put(actorId, favor);
		return favor;
	}

	@Override
	public void update(Favor favor) {
		dbQueue.updateQueue(favor);
	}

	@Override
	public int cleanCache(long actorId) {
		FAVOR_MAP.remove(actorId);
		return FAVOR_MAP.size();
	}


}
