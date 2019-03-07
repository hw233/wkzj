package com.jtang.gameserver.module.luckstar.dao.impl;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.LuckStar;
import com.jtang.gameserver.module.luckstar.dao.LuckStarDao;

@Component
public class LuckStarDaoImpl implements LuckStarDao {

	private static ConcurrentLinkedHashMap<Long, LuckStar> LUCKSTAR_MAP = new ConcurrentLinkedHashMap.Builder<Long, LuckStar>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;

	@Override
	public LuckStar get(long actorId) {
		if (LUCKSTAR_MAP.containsKey(actorId)) {
			return LUCKSTAR_MAP.get(actorId);
		}
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("actorId", actorId);
		LuckStar luckStar = jdbc.getFirst(LuckStar.class, map);
		if (luckStar == null) {
			luckStar = LuckStar.valueOf(actorId);
		}
		LUCKSTAR_MAP.put(actorId, luckStar);
		return luckStar;
	}

	@Override
	public void update(LuckStar luckStar) {
		dbQueue.updateQueue(luckStar);
	}

}
