package com.jtang.gameserver.module.extapp.deitydesc.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.DeityDescend;
import com.jtang.gameserver.module.extapp.deitydesc.dao.DeityDescendDao;

@Component
public class DeityDescendDaoImpl implements DeityDescendDao, CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	
	@Autowired
	DBQueue dbQueue;
	
	/**
	 * 角色id
	 */
	private static ConcurrentLinkedHashMap<Long, DeityDescend> DEITY_DESCEND_MAPS = new ConcurrentLinkedHashMap.Builder<Long, DeityDescend>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public int cleanCache(long actorId) {
		DEITY_DESCEND_MAPS.remove(actorId);
		return DEITY_DESCEND_MAPS.size();
	}

	@Override
	public DeityDescend get(long actorId) {
		if (DEITY_DESCEND_MAPS.containsKey(actorId)) {
			return DEITY_DESCEND_MAPS.get(actorId);
		}

		DeityDescend deityDescend = jdbc.get(DeityDescend.class, actorId);
		if (deityDescend == null){
			deityDescend = DeityDescend.valueOf(actorId);
		}
		DEITY_DESCEND_MAPS.put(actorId, deityDescend);
		return deityDescend;
	}

	@Override
	public boolean update(DeityDescend deityDescend) {
		dbQueue.updateQueue(deityDescend);
		return true;
	}

}
