package com.jtang.gameserver.module.goods.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Goods;
import com.jtang.gameserver.module.goods.dao.GoodsDao;

@Repository
public class GoodsDaoImpl implements GoodsDao, CacheListener {
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, Goods> GOODS_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Goods>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Goods get(long actorId) {
		if (GOODS_MAPS.containsKey(actorId)) {
			return GOODS_MAPS.get(actorId);
		}
		Goods goods = jdbc.get(Goods.class, actorId);
		if (goods == null) {
			goods = Goods.valueOf(actorId);
		}
		GOODS_MAPS.put(actorId, goods);
		return goods;
	}

	public boolean update(Goods goods) {
		dbQueue.updateQueue(goods);
		return true;
	}

	@Override
	public int cleanCache(long actorId) {
		GOODS_MAPS.remove(actorId);
		return GOODS_MAPS.size();
	}

}
