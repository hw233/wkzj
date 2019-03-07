package com.jtang.gameserver.module.treasure.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Treasure;
import com.jtang.gameserver.module.treasure.dao.TreasureDao;
import com.jtang.gameserver.module.treasure.model.TreasureVO;

@Component
public class TreasureDaoImpl implements TreasureDao, CacheListener {

	private static ConcurrentLinkedHashMap<Long, TreasureVO> TREASURE_VO_MAP = new ConcurrentLinkedHashMap.Builder<Long, TreasureVO>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	private static ConcurrentLinkedHashMap<Long, Treasure> TREASURE_MAP = new ConcurrentLinkedHashMap.Builder<Long, Treasure>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;

	@Override
	public TreasureVO getTreasureVO(long actorId,int level) {
		if (TREASURE_VO_MAP.containsKey(actorId)) {
			return TREASURE_VO_MAP.get(actorId);
		}
		TreasureVO treasureVO = TreasureVO.valueOf(actorId,level);
		TREASURE_VO_MAP.put(actorId, treasureVO);
		return treasureVO;
	}

	@Override
	public Treasure getTreasure(long actorId) {
		if (TREASURE_MAP.containsKey(actorId)) {
			return TREASURE_MAP.get(actorId);
		}
		Treasure treasure = jdbc.get(Treasure.class, actorId);
		if (treasure == null) {
			treasure = Treasure.valueOf(actorId);
		}
		TREASURE_MAP.put(actorId, treasure);
		return treasure;
	}

	@Override
	public void update(Treasure treasure) {
		treasure.operationTime = TimeUtils.getNow();
		dbQueue.updateQueue(treasure);
	}

	@Override
	public int cleanCache(long actorId) {
		TREASURE_MAP.remove(actorId);
		return TREASURE_MAP.size();
	}

	@Override
	public void clean() {
		TREASURE_VO_MAP.clear();
	}

}
