package com.jtang.gameserver.module.demon.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.DemonRank;
import com.jtang.gameserver.module.demon.dao.DemonRankDao;
@Component
public class DemonRankDaoImpl implements DemonRankDao{

	private static final Logger LOGGER = LoggerFactory.getLogger(DemonRankDaoImpl.class);
	@Autowired
	private IdTableJdbc jdbc;
	
	@Autowired
	private DBQueue dbQueue;
	
	/**
	 * key:角色id
	 */
	private static ConcurrentLinkedHashMap<Long, DemonRank> DEMON_RANK_MAPS = new ConcurrentLinkedHashMap.Builder<Long, DemonRank>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	
	@PostConstruct
	private void init() {
		List<DemonRank> list = jdbc.getList(DemonRank.class);
		for (DemonRank demonRank : list) {
			DEMON_RANK_MAPS.put(demonRank.getPkId(), demonRank);
		}
	}
	@Override
	public DemonRank get(long actorId) {
		if (DEMON_RANK_MAPS.containsKey(actorId)) {
			return DEMON_RANK_MAPS.get(actorId);
		}
		return null;
	}

	@Override
	public boolean update(List<DemonRank> demonRanks) {
		synchronized (DEMON_RANK_MAPS) {
			DEMON_RANK_MAPS.clear();
			try {
				jdbc.update("delete from demonRank");
			} catch (DataAccessException e) {
				LOGGER.error("{}", e);
			}
			for (DemonRank demonRank : demonRanks) {
				DEMON_RANK_MAPS.put(demonRank.getPkId(), demonRank);
				dbQueue.updateQueue(demonRank);
			}
		}
		return true;
	}

	
	@Override
	public List<DemonRank> getByCondition(long difficult, int rankNum) {
			
		List<DemonRank> demonRanks = new ArrayList<>();
		for (DemonRank d : DEMON_RANK_MAPS.values()) {
			if (d.lastDifficult == difficult) {
				demonRanks.add(d);
			}
		}
		Collections.sort(demonRanks);
		if (rankNum > demonRanks.size()) {
			rankNum = demonRanks.size();
		}
		return demonRanks.subList(0, rankNum);
	}
	
}
