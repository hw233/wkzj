package com.jtang.gameserver.module.sprintgift.dao.impl;

import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.SprintGift;
import com.jtang.gameserver.module.sprintgift.dao.SprintGiftDao;


@Component
public class SprintGiftDaoImpl implements SprintGiftDao, CacheListener{

	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	
	/**
	 * 冲级礼包数据缓存
	 * 格式是:
	 * ConcurrentMap<actorId, SprintGift>
	 */
	private static ConcurrentMap<Long, SprintGift> SPRINTGIFTS = new ConcurrentLinkedHashMap.Builder<Long, SprintGift>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();

	
	@Override
	public SprintGift get(long actorId) {
		if (SPRINTGIFTS.containsKey(actorId)) {
			return SPRINTGIFTS.get(actorId);
		}
		SprintGift gift = jdbc.get(SprintGift.class, actorId);
		if (gift == null) {
			gift = SprintGift.valueOf(actorId);
		}
		SPRINTGIFTS.put(actorId, gift);
		return gift;
	}

	@Override
	public boolean update(SprintGift gift) {
		dbQueue.updateQueue(gift);
		return true;
	}

	@Override
	public int cleanCache(long actorId) {
		SPRINTGIFTS.remove(actorId);
		return SPRINTGIFTS.size();
	}

}
