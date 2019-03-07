package com.jtang.gameserver.module.trialcave.dao.impl;

import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.TrialCave;
import com.jtang.gameserver.module.trialcave.dao.TrialCaveDao;

/**
 * 试炼洞DAO
 * @author lig
 *
 */
@Component
public class TrialCaveDaoImpl implements TrialCaveDao, CacheListener {
	
	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	
	/**
	 * 故事的LRU缓存列表.
	 * 格式是:
	 * ConcurrentMap<actorId, {@code TrialCave}>
	 */
	private static ConcurrentMap<Long, TrialCave> TRIAL_CAVES = new ConcurrentLinkedHashMap.Builder<Long, TrialCave>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public TrialCave get(long actorId) {
		if (TRIAL_CAVES.containsKey(actorId)) {
			return TRIAL_CAVES.get(actorId);
		}

		TrialCave cave = jdbc.get(TrialCave.class, actorId);
		if (cave == null) {
			cave = TrialCave.valueOf(actorId);
		}
		TRIAL_CAVES.put(actorId, cave);
		return cave;
	}

	@Override
	public void update(TrialCave cave) {
		dbQueue.updateQueue(cave);
	}

	@Override
	public int cleanCache(long actorId) {
		TRIAL_CAVES.remove(actorId);
		return TRIAL_CAVES.size();
	}
}
