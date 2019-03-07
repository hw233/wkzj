package com.jtang.gameserver.module.extapp.ernie.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Ernie;
import com.jtang.gameserver.module.extapp.ernie.dao.ErnieDao;

@Component
public class ErnieDaoImpl implements ErnieDao, CacheListener {

	@Autowired
	IdTableJdbc jdbc;

	@Autowired
	DBQueue dbQueue;

	private static ConcurrentLinkedHashMap<Long, Ernie> ERNIE_MAP = new ConcurrentLinkedHashMap.Builder<Long, Ernie>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public int cleanCache(long actorId) {
		ERNIE_MAP.remove(actorId);
		return ERNIE_MAP.size();
	}

	@Override
	public Ernie get(long actorId) {
		if (ERNIE_MAP.containsKey(actorId)) {
			return ERNIE_MAP.get(actorId);
		}
		Ernie ernie = jdbc.get(Ernie.class, actorId);
		if (ernie == null) {
			ernie = Ernie.valueOf(actorId);
		}
		ERNIE_MAP.put(actorId, ernie);
		return ernie;
	}

	@Override
	public boolean update(Ernie ernie) {
		dbQueue.updateQueue(ernie);
		return true;
	}

}
