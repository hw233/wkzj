package com.jtang.gameserver.module.hero.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.HeroSoul;
import com.jtang.gameserver.module.hero.dao.HeroSoulDao;
import com.jtang.gameserver.module.hero.model.HeroSoulVO;

/**
 * 
 * @author 0x737263
 *
 */
@Repository
public class HeroSoulDaoImpl implements HeroSoulDao, CacheListener {	

	@Autowired
	private IdTableJdbc jdbc;	
	
	@Autowired
	private DBQueue dbQueue;
	
	/**
	 * 仙人魂魄的LRU缓存列表.
	 * 格式:ConcurrentMap<actorId, HeroSoul>
	 */
	private static ConcurrentLinkedHashMap<Long, HeroSoul> HERO_SOURLS = new ConcurrentLinkedHashMap.Builder<Long, HeroSoul>()
			.maximumWeightedCapacity(Short.MAX_VALUE).build();
	
	@Override
	public HeroSoul get(long actorId) {
		if (HERO_SOURLS.containsKey(actorId)) {
			return HERO_SOURLS.get(actorId);
		}

		HeroSoul soul = jdbc.get(HeroSoul.class, actorId);
		if (soul == null) {
			soul = HeroSoul.valueOf(actorId);
		}
		HERO_SOURLS.put(actorId, soul);
		return soul;
	}
	
	@Override
	public boolean update(HeroSoul heroSoul) {
		dbQueue.updateQueue(heroSoul);
		return true; // jdbc.update(heroSoul) > 0 ? true : false;
	}

	@Override
	public List<HeroSoulVO> getList(long actorId) {
		HeroSoul soul = get(actorId);
		Map<Integer, Integer> map = soul.getHeroSoulMap();
	
		List<HeroSoulVO> result = new ArrayList<HeroSoulVO>();
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			result.add(HeroSoulVO.valueOf(entry.getKey(), entry.getValue()));
		}
		return result;
	}

	@Override
	public int cleanCache(long actorId) {
		HERO_SOURLS.remove(actorId);
		return HERO_SOURLS.size();
	}
	
	
}
