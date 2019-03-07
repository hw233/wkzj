package com.jtang.gameserver.module.power.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.PowerExt;
import com.jtang.gameserver.module.power.dao.PowerExtDao;

@Component
public class PowerExtDaoImpl implements PowerExtDao,CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, PowerExt> POWER_MAP = new ConcurrentLinkedHashMap.Builder<Long, PowerExt>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public PowerExt getPowerExt(long actorId) {
		if(POWER_MAP.containsKey(actorId)){
			return POWER_MAP.get(actorId);
		}
		PowerExt powerExt = jdbc.get(PowerExt.class, actorId);
		if(powerExt == null){
			powerExt = PowerExt.valueOf(actorId);
		}
		POWER_MAP.put(actorId, powerExt);
		return powerExt;
	}

	@Override
	public void update(PowerExt powerExt) {
		dbQueue.updateQueue(powerExt);
	}

	@Override
	public int cleanCache(long actorId) {
		POWER_MAP.remove(actorId);
		return POWER_MAP.size();
	}
	
	
}
