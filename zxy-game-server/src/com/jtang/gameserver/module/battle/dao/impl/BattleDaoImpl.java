package com.jtang.gameserver.module.battle.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Battle;
import com.jtang.gameserver.module.battle.dao.BattleDao;

@Component
public class BattleDaoImpl implements BattleDao, CacheListener {

	@Autowired
	IdTableJdbc jdbcTemplate;
	@Autowired
	DBQueue dbQueue;
	
	
	private static ConcurrentLinkedHashMap<Long, Battle> BATTLE_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Battle>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Battle get(long actorId) {
		if (BATTLE_MAPS.containsKey(actorId)){
			return BATTLE_MAPS.get(actorId);
		}
		Battle battle = jdbcTemplate.get(Battle.class, actorId);
		if (battle == null){
			battle = Battle.valueOf(actorId);
		}
		BATTLE_MAPS.put(actorId, battle);
		return battle;
	}

	@Override
	public boolean update(long actorId) {
		Battle battle = get(actorId);
		dbQueue.updateQueue(battle);
		return true;
		// return jdbcTemplate.update(battle) > 0;
	}

	@Override
	public int cleanCache(long actorId) {
		BATTLE_MAPS.remove(actorId);
		return BATTLE_MAPS.size();
	}

}
