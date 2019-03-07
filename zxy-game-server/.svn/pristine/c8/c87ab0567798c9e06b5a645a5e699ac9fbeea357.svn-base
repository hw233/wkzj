package com.jtang.gameserver.module.herobook.dao.impl;

import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dataconfig.service.HeroBookRewardService;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.HeroBook;
import com.jtang.gameserver.module.herobook.dao.HeroBookDao;

@Component
public class HeroBookDaoImpl implements HeroBookDao, CacheListener {

	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	
	/**
	 * 仙人图鉴的LRU缓存列表.
	 * 格式是:
	 * ConcurrentMap<actorId, HeroBook>
	 */
	private static ConcurrentMap<Long, HeroBook> HERO_BOOKS = new ConcurrentLinkedHashMap.Builder<Long, HeroBook>().maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	@Override
	public HeroBook get(long actorId) {
		if (HERO_BOOKS.containsKey(actorId)) {
			return HERO_BOOKS.get(actorId);
		}
		HeroBook heroBook = jdbc.get(HeroBook.class, actorId);
		if (heroBook == null) {
			heroBook = HeroBook.valueOf(actorId, HeroBookRewardService.getStartNode());
		}
		HERO_BOOKS.put(actorId, heroBook);
		return heroBook;
	}

	@Override
	public boolean update(HeroBook heroBook) {
		dbQueue.updateQueue(heroBook);
		return true;
	}
	
	@Override
	public int cleanCache(long actorId) {
		HERO_BOOKS.remove(actorId);
		return HERO_BOOKS.size();
	}

}
