package com.jtang.gameserver.module.adventures.shop.trader.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Trader;
import com.jtang.gameserver.module.adventures.shop.trader.dao.TraderDao;

@Component
public class TraderDaoImpl implements TraderDao,CacheListener {
	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, Trader> TRADER_MAP = new ConcurrentLinkedHashMap.Builder<Long, Trader>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public Trader get(long actorId) {
		if(TRADER_MAP.containsKey(actorId)){
			return TRADER_MAP.get(actorId);
		}
		Trader trader = jdbc.get(Trader.class, actorId);
		if(trader == null){
			trader = Trader.valueOf(actorId);
		}
		TRADER_MAP.put(actorId, trader);
		return trader;
	}

	@Override
	public void update(Trader trader) {
		dbQueue.updateQueue(trader);
	}

	@Override
	public int cleanCache(long actorId) {
		TRADER_MAP.remove(actorId);
		return TRADER_MAP.size();
	}

}
