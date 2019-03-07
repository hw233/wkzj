package com.jtang.gameserver.module.love.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.LoveShop;
import com.jtang.gameserver.module.love.dao.LoveShopDao;

@Component
public class LoveShopDaoImpl implements LoveShopDao,CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, LoveShop> SHOP_MAP = new ConcurrentLinkedHashMap.Builder<Long, LoveShop>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public LoveShop get(long actorId) {
		if(SHOP_MAP.containsKey(actorId)){
			return SHOP_MAP.get(actorId);
		}
		LoveShop loveShop = jdbc.get(LoveShop.class, actorId);
		if(loveShop == null){
			loveShop = LoveShop.valueOf(actorId);
		}
		SHOP_MAP.put(actorId, loveShop);
		return SHOP_MAP.get(actorId);
	}

	@Override
	public int cleanCache(long actorId) {
		SHOP_MAP.remove(actorId);
		return SHOP_MAP.size();
	}

	@Override
	public void update(LoveShop loveShop) {
		dbQueue.updateQueue(loveShop);
	}

	@Override
	public void remove(LoveShop loveShop) {
		jdbc.delete(loveShop);
		SHOP_MAP.remove(loveShop.actorId);
	}
	
}
