package com.jtang.gameserver.module.story.dao.impl;

import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Stories;
import com.jtang.gameserver.module.story.dao.StoryDao;

/**
 * 数据访问层接口
 * @author vinceruan
 *
 */
@Repository
public class StoryDaoImpl implements StoryDao, CacheListener {
	
	@Autowired
	private IdTableJdbc jdbc;
	
	@Autowired
	private DBQueue dbQueue;
	
	/**
	 * 故事的LRU缓存列表.
	 * 格式是:
	 * ConcurrentMap<actorId, Stories>
	 */
	private static ConcurrentMap<Long, Stories> STORIES = new ConcurrentLinkedHashMap.Builder<Long, Stories>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Stories get(long actorId) {
		if (STORIES.containsKey(actorId)) {
			return STORIES.get(actorId);
		}
		Stories story = jdbc.get(Stories.class, actorId);
		if (story == null) {
			story = Stories.valueOf(actorId);
		}
		STORIES.put(actorId, story);
		return story;
	}

	@Override
	public void update(Stories story) {
		dbQueue.updateQueue(story);
	}

	@Override
	public int cleanCache(long actorId) {
		STORIES.remove(actorId);
		return STORIES.size();
	}
}
