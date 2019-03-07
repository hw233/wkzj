package com.jtang.gameserver.module.extapp.basin.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Basin;
import com.jtang.gameserver.module.extapp.basin.dao.BasinDao;

@Component
public class BasinDaoImpl implements BasinDao,CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, Basin> BASIN_MAP = new ConcurrentLinkedHashMap.Builder<Long, Basin>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public int cleanCache(long actorId) {
		BASIN_MAP.remove(actorId);
		return BASIN_MAP.size();
	}

	@Override
	public Basin get(long actorId) {
		if(BASIN_MAP.containsKey(actorId)){
			return BASIN_MAP.get(actorId);
		}
		Basin basin = jdbc.get(Basin.class, actorId);
		if(basin == null){
			basin = Basin.valueOf(actorId);
		}
		BASIN_MAP.put(actorId, basin);
		return basin;
	}

	@Override
	public void update(Basin basin) {
		basin.operationTime = TimeUtils.getNow();
		dbQueue.updateQueue(basin);
	}

}
