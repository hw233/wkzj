package com.jtang.gameserver.module.adventures.shop.shop.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Shop;
import com.jtang.gameserver.module.adventures.shop.shop.dao.ShopDao;
import com.jtang.gameserver.module.adventures.shop.shop.model.ShopVO;

@Repository
public class ShopDaoImpl implements ShopDao, CacheListener {
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, Shop> SHOP_MAP = new ConcurrentLinkedHashMap.Builder<Long, Shop>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Shop get(long actorId) {
		if (SHOP_MAP.containsKey(actorId)) {
			return SHOP_MAP.get(actorId);
		}
		Shop shop = jdbc.get(Shop.class, actorId);
		if (shop == null) {
			shop = Shop.valueOf(actorId);
		}
		SHOP_MAP.put(actorId, shop);
		return shop;
	}

	@Override
	public boolean updateShopVO(long actorId, ShopVO shopVO) {
		Shop shop = get(actorId);
		shop.parseToShop(shopVO);
		shop.buyTime = TimeUtils.getNow();
		updateShop(shop);
		return true;
	}

	@Override
	public void updateShop(Shop shop) {
		dbQueue.updateQueue(shop);
	}

	@Override
	public int cleanCache(long actorId) {
		SHOP_MAP.remove(actorId);
		return SHOP_MAP.size();
	}

}
