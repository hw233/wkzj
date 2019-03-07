package com.jtang.gameserver.module.extapp.onlinegifts.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.OnlineGifts;
import com.jtang.gameserver.module.extapp.onlinegifts.dao.OnlineGiftsDao;

@Component
public class OnlineGiftsDaoImpl implements OnlineGiftsDao,CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, OnlineGifts> ONLINEGIFTS_MAP = new ConcurrentLinkedHashMap.Builder<Long, OnlineGifts>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	

	@Override
	public OnlineGifts get(long actorId) {
		if(ONLINEGIFTS_MAP.containsKey(actorId)){
			return ONLINEGIFTS_MAP.get(actorId);
		}
		OnlineGifts gifts = jdbc.get(OnlineGifts.class, actorId);
		if(gifts == null){
			gifts = OnlineGifts.valueOf(actorId);
			update(gifts);
		}
		ONLINEGIFTS_MAP.put(actorId, gifts);
		return gifts;
	}

	@Override
	public boolean update(OnlineGifts gifts) {
		dbQueue.updateQueue(gifts);
		return true;
	}
	
	@Override
	public int cleanCache(long actorId) {
		ONLINEGIFTS_MAP.remove(actorId);
		return ONLINEGIFTS_MAP.size();
	}
}
