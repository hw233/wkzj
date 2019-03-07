package com.jtang.gameserver.module.adventures.shop.vipshop.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.VipShop;
import com.jtang.gameserver.module.adventures.shop.vipshop.dao.VipShopDao;

@Component
public class VipShopDaoImpl implements VipShopDao,CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, VipShop> VIPSHOP_MAP = new ConcurrentLinkedHashMap.Builder<Long, VipShop>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public VipShop get(long actorId) {
		if(VIPSHOP_MAP.containsKey(actorId)){
			return VIPSHOP_MAP.get(actorId);
		}
		VipShop vipShop = jdbc.get(VipShop.class, actorId);
		if(vipShop == null){
			vipShop = VipShop.valueOf(actorId);
		}
		VIPSHOP_MAP.put(actorId, vipShop);
		return vipShop;
	}

	@Override
	public int cleanCache(long actorId) {
		VIPSHOP_MAP.remove(actorId);
		return VIPSHOP_MAP.size();
	}

	@Override
	public void update(VipShop vipShop) {
		dbQueue.updateQueue(vipShop);
	}

}
