package com.jtang.gameserver.module.recruit.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Recruit;
import com.jtang.gameserver.module.recruit.dao.RecruitDao;

/**
 * 聚仙阵数据访问实现
 * @author 0x737263
 *
 */
@Repository
public class RecruitDaoImpl implements RecruitDao, CacheListener {
	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, Recruit> RECRUIT_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Recruit>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
		
	@Override
	public Recruit get(long actorId) {
		Recruit entity = RECRUIT_MAPS.get(actorId);
		if (entity == null) {
			entity = jdbc.get(Recruit.class, actorId);
			if (entity == null) {
				entity = Recruit.valueOf(actorId);
			}
			RECRUIT_MAPS.put(actorId, entity);
		}

		return entity;
	}

	@Override
	public int cleanCache(long actorId) {
		RECRUIT_MAPS.remove(actorId);
		return RECRUIT_MAPS.size();
	}

	@Override
	public boolean update(Recruit entity) {
		dbQueue.updateQueue(entity);
		return true;
	}
	
}
