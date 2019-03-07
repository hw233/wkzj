package com.jtang.gameserver.module.gift.dao.impl;

import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Gift;
import com.jtang.gameserver.module.gift.dao.GiftDao;

@Component
public class GiftDaoImpl implements GiftDao, CacheListener {
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	/**
	 * 故事的LRU缓存列表.
	 * 格式是:
	 * ConcurrentMap<actorId, {@code Gift}>
	 */
	private static ConcurrentMap<Long, Gift> GIFTS = new ConcurrentLinkedHashMap.Builder<Long, Gift>().maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE)
			.build();

	@Override
	public Gift get(long actorId) {
		if (GIFTS.containsKey(actorId)) {
			return GIFTS.get(actorId);
		}
		Gift gift = jdbc.get(Gift.class, actorId);
		if (gift == null){
			gift = Gift.valueOf(actorId);
		}
		GIFTS.put(actorId, gift);
		
		return gift;
	}

	@Override
	public boolean update(Gift gift) {
		dbQueue.updateQueue(gift);
		return true;
	}
	
	@Override
	public int cleanCache(long actorId) {
		GIFTS.remove(actorId);
		return GIFTS.size();
	}
}
