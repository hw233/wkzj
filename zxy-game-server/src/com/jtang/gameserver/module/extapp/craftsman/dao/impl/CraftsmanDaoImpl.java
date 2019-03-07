package com.jtang.gameserver.module.extapp.craftsman.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Craftsman;
import com.jtang.gameserver.module.extapp.craftsman.dao.CraftsmanDao;

@Component
public class CraftsmanDaoImpl implements CraftsmanDao, CacheListener  {

	@Autowired
	IdTableJdbc jdbc;
	
	@Autowired
	DBQueue dbQueue;
	/**
	 * 角色id
	 */
	private static ConcurrentLinkedHashMap<Long, Craftsman> CRAFTSMAN_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Craftsman>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	@Override
	public Craftsman get(long actorId) {
		if (CRAFTSMAN_MAPS.containsKey(actorId)) {
			return CRAFTSMAN_MAPS.get(actorId);
		}

		Craftsman craftsman = jdbc.get(Craftsman.class, actorId);
		if (craftsman == null){
			craftsman = Craftsman.valueOf(actorId);
		}
		CRAFTSMAN_MAPS.put(actorId, craftsman);
		return craftsman;
	}

	
	@Override
	public boolean update(Craftsman craftsman) {
		dbQueue.updateQueue(craftsman);
		return true;
	}
	
	@Override
	public int cleanCache(long actorId) {
		CRAFTSMAN_MAPS.remove(actorId);
		return CRAFTSMAN_MAPS.size();
	}

}
