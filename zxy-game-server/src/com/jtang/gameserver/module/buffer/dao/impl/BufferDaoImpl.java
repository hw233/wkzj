package com.jtang.gameserver.module.buffer.dao.impl;

import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Buffers;
import com.jtang.gameserver.module.buffer.dao.BufferDao;

/**
 * 仙人buffer的数据访问接口
 * @author vinceruan
 *
 */
@Repository
public class BufferDaoImpl implements BufferDao, CacheListener {
	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	/**
	 * 仙人的LRU缓存列表.
	 * 格式是:
	 * ConcurrentMap<actorId, Map<HeroId, Hero>>
	 */
	ConcurrentMap<Long, Buffers> BUFFERS = new ConcurrentLinkedHashMap.Builder<Long, Buffers>().maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Buffers get(long actorId) {
		if (BUFFERS.containsKey(actorId)) {
			return BUFFERS.get(actorId);
		}
		
		Buffers	userBuffer = this.queryUserBuffer(actorId);
		if (userBuffer == null) {
			userBuffer = Buffers.valueOf(actorId);
		}
		BUFFERS.put(actorId, userBuffer);
		return userBuffer;
	}
	
	private Buffers queryUserBuffer(long actorId) {
		LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("actorid", actorId);
		return jdbc.getFirst(Buffers.class, condition);
	}

	@Override
	public boolean update(long actorId) {
		Buffers buffer = get(actorId);
		dbQueue.updateQueue(buffer);
		return true;
		//return this.jdbc.update(buffer) > 0;
	}

	@Override
	public int cleanCache(long actorId) {
		BUFFERS.remove(actorId);
		return BUFFERS.size();
	}
}
