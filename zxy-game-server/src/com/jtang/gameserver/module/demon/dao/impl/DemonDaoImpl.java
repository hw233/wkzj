package com.jtang.gameserver.module.demon.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Demon;
import com.jtang.gameserver.module.demon.dao.DemonDao;
@Component
public class DemonDaoImpl implements DemonDao, CacheListener {

	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	/**
	 * 角色id
	 */
	private static ConcurrentLinkedHashMap<Long, Demon> DEMON_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Demon>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	@Override
	public Demon get(long actorId) {
		if (DEMON_MAPS.containsKey(actorId)) {
			return DEMON_MAPS.get(actorId);
		}

		Demon demon = jdbc.get(Demon.class, actorId);
		if (demon == null){
			demon = Demon.valueOf(actorId);
		}
		DEMON_MAPS.put(actorId, demon);
		return demon;
	}


	@Override
	public boolean update(Demon demon) {
		dbQueue.updateQueue(demon);
		return true;
	}
	
	@Override
	public int cleanCache(long actorId) {
		DEMON_MAPS.remove(actorId);
		return DEMON_MAPS.size();
	}
}
