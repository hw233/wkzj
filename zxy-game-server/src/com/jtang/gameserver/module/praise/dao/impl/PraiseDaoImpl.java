package com.jtang.gameserver.module.praise.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Praise;
import com.jtang.gameserver.module.praise.dao.PraiseDao;
@Component
public class PraiseDaoImpl implements PraiseDao, CacheListener {
	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	
	/**
	 * 角色id
	 */
	private static ConcurrentLinkedHashMap<Long, Praise> PRAISE_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Praise>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	@Override
	public Praise get(long actorId) {
		if (PRAISE_MAPS.containsKey(actorId)) {
			return PRAISE_MAPS.get(actorId);
		}

		Praise praise = jdbc.get(Praise.class, actorId);
		if (praise == null){
			praise = Praise.valueOf(actorId);
		}
		PRAISE_MAPS.put(actorId, praise);
		return praise;
	}


	@Override
	public boolean update(Praise praise) {
		dbQueue.updateQueue(praise);
		return true;
	}

	@Override
	public int cleanCache(long actorId) {
		PRAISE_MAPS.remove(actorId);
		return PRAISE_MAPS.size();
	}

}
