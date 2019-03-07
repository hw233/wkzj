package com.jtang.gameserver.module.hero.dao.impl;

import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.context.SpringContext;
import com.jtang.core.db.DBQueue;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Heros;
import com.jtang.gameserver.module.hero.dao.HeroDao;

/**
 * 
 * @author 0x737263
 *
 */
@Repository
public class HeroDaoImpl implements HeroDao, CacheListener {

	@Autowired
	private IdTableJdbc jdbc;
	
	@Autowired
	private DBQueue dbQueue;
	
	/**
	 * 仙人的LRU缓存列表.
	 * 格式是:
	 * ConcurrentMap<actorId, Heros>
	 */
	private static ConcurrentMap<Long, Heros> HEROS = new ConcurrentLinkedHashMap.Builder<Long, Heros>().maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public Heros get(long actorId) {
		if (HEROS.containsKey(actorId)) {
			return HEROS.get(actorId);
		}
		Heros heros = jdbc.get(Heros.class, actorId);
		if (heros == null) {
			heros = Heros.valueOf(actorId, null);
		}
		HEROS.put(actorId, heros);
		return heros;
	}

	@Override
	public boolean update(Heros heros) {
		dbQueue.updateQueue(heros);
		return true;
	}

	@Override
	public void recordCompose(long actorId) {
		Heros heros = get(actorId);
		heros.composeTime = TimeUtils.getNow();
		heros.composeNum++;
		update(heros);
	}

	@Override
	public boolean canCompose(long actorId, int num) {
		Heros heros = get(actorId);
		if (heros.composeNum < num){
			return true;
		}
		return false;
	}

	@Override
	public void checkAndReset(long actorId) {
		Heros heros = get(actorId);
		if (TimeUtils.beforeTodayZero(heros.composeTime)){
			heros.composeNum = 0;
		}
		update(heros);
	}

	@Override
	public int cleanCache(long actorId) {
		HEROS.remove(actorId);
		return HEROS.size();
	}
	public static void main(String[] args) {
		HeroDaoImpl dao = (HeroDaoImpl) SpringContext.getBean(HeroDaoImpl.class);
		Heros heros = dao.get(41984984041L);
		for (HeroVO vo : heros.getHeroVOMap().values()) {
			vo.atk = 10000;
			vo.defense = 1000;
			vo.hp = 10000;
			
		}
		dao.update(heros);
	}

}
