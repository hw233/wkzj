package com.jtang.gameserver.module.love.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Love;
import com.jtang.gameserver.module.love.dao.LoveDao;

@Component
public class LoveDaoImpl implements LoveDao,CacheListener {

	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, Love> LOVE_MAP = new ConcurrentLinkedHashMap.Builder<Long, Love>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	@Override
	public Love get(long actorId) {
		if(LOVE_MAP.containsKey(actorId)){
			return LOVE_MAP.get(actorId);
		}
		Love love = jdbc.get(Love.class, actorId);
		if(love == null){
			love = Love.valueOf(actorId);
		}
		LOVE_MAP.put(actorId, love);
		return love;
	}

	@Override
	public boolean update(Love love) {
		dbQueue.updateQueue(love);
		return true;
	}

	@Override
	public int cleanCache(long actorId) {
		LOVE_MAP.remove(actorId);
		return LOVE_MAP.size();
	}

}
