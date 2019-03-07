package com.jtang.gameserver.module.extapp.beast.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Beast;
import com.jtang.gameserver.module.extapp.beast.dao.BeastDao;
@Repository
public class BeastDaoImpl implements BeastDao, CacheListener {

	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	/**
	 * 角色id
	 */
	private static ConcurrentLinkedHashMap<Long, Beast> BEAST_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Beast>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	@Override
	public Beast get(long actorId) {
		if (BEAST_MAPS.containsKey(actorId)) {
			return BEAST_MAPS.get(actorId);
		}

		Beast beast = jdbc.get(Beast.class, actorId);
		if (beast == null){
			beast = Beast.valueOf(actorId);
		}
		BEAST_MAPS.put(actorId, beast);
		return beast;
	}


	@Override
	public boolean update(Beast beast) {
		dbQueue.updateQueue(beast);
		return true;
	}
	
	@Override
	public int cleanCache(long actorId) {
		BEAST_MAPS.remove(actorId);
		return BEAST_MAPS.size();
	}
}
