package com.jtang.gameserver.module.delve.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Delve;
import com.jtang.gameserver.module.delve.dao.DelveDao;

@Repository
public class DelveDaoImpl implements DelveDao, CacheListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(DelveDaoImpl.class);
	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, Delve> DELVE_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Delve>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public Delve get(long actorId) {
		if (DELVE_MAPS.containsKey(actorId)) {
			return DELVE_MAPS.get(actorId);
		}

		Delve delve = jdbc.get(Delve.class, actorId);
		if (delve == null){
			delve = Delve.valueOf(actorId);
		}
		DELVE_MAPS.put(actorId, delve);
		return delve;
	}


	@Override
	public boolean update(long actorId, int level) {
		Delve delve = get(actorId);
		if (delve != null) {
			delve.level = level;
			dbQueue.updateQueue(delve);
			// jdbc.update(delve);
			return true;
		} else {
			LOGGER.error(String.format("更新错误，actorId:[%s],level: [%s]", actorId, level));
		}
		return false;
	}


	@Override
	public int cleanCache(long actorId) {
		DELVE_MAPS.remove(actorId);
		return DELVE_MAPS.size();
	}

}
