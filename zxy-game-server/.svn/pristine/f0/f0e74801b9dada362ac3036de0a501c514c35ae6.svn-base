package com.jtang.gameserver.module.lineup.dao.impl;

import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.module.lineup.dao.LineupDao;

/**
 * 阵型数据访问接口
 * @author vinceruan
 *
 */
@Repository
public class LineupDaoImpl implements LineupDao, CacheListener {
	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	/**
	 * 阵型信息LRU缓存
	 */
	private static ConcurrentMap<Long, Lineup> LINEUPS = new ConcurrentLinkedHashMap.Builder<Long, Lineup>().maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE)
			.build();
	
	@Override
	public Lineup getLineup(long actorId) {
		if (LINEUPS.containsKey(actorId)) {
			return LINEUPS.get(actorId);
		}

		Lineup lineup = jdbc.get(Lineup.class, actorId);
		if (lineup == null) {
			lineup = Lineup.valueOf(actorId);
		}
		LINEUPS.put(actorId, lineup);
		return LINEUPS.get(actorId);
	}

	@Override
	public void updateLineup(Lineup lineup) {
		dbQueue.updateQueue(lineup);
	}
	
	@Override
	public int cleanCache(long actorId) {
		LINEUPS.remove(actorId);
		return LINEUPS.size();
	}
}
