package com.jtang.gameserver.module.love.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.LoveMonster;
import com.jtang.gameserver.module.love.dao.LoveMonsterDao;

@Component
public class LoveMonsterDaoImpl implements LoveMonsterDao,CacheListener {
	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, LoveMonster> LOVE_MONSTER_MAP1 = new ConcurrentLinkedHashMap.Builder<Long, LoveMonster>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	private static ConcurrentLinkedHashMap<Long, LoveMonster> LOVE_MONSTER_MAP2 = new ConcurrentLinkedHashMap.Builder<Long, LoveMonster>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public LoveMonster get(long actorId1, long actorId2) {
		if(LOVE_MONSTER_MAP1.containsKey(actorId1)){
			return LOVE_MONSTER_MAP1.get(actorId1);
		}
		if(LOVE_MONSTER_MAP2.containsKey(actorId1)){
			return LOVE_MONSTER_MAP2.get(actorId1);
		}
		String sql = "select * from loveMonster where loveId1 = ? or loveId2 = ?";
		List<LoveMonster> list = jdbc.getList(sql, new Object[]{actorId1,actorId1}, LoveMonster.class);
		if(list.size() == 0){
			LoveMonster loveMonster = LoveMonster.valueOf(actorId1,actorId2);
			list.add(loveMonster);
		}
		LOVE_MONSTER_MAP1.put(actorId1, list.get(0));
		LOVE_MONSTER_MAP2.put(actorId2, list.get(0));
		return LOVE_MONSTER_MAP1.get(actorId1);
	}

	@Override
	public void update(LoveMonster loveMonster) {
		loveMonster.fightTime = TimeUtils.getNow();
		dbQueue.updateQueue(loveMonster);
	}

	@Override
	public void remove(LoveMonster loveMonster) {
		jdbc.delete(loveMonster);
		if(LOVE_MONSTER_MAP1.containsKey(loveMonster.loveId1)){
			LOVE_MONSTER_MAP1.remove(loveMonster.loveId1);
			LOVE_MONSTER_MAP2.remove(loveMonster.loveId2);
		}else{
			LOVE_MONSTER_MAP2.remove(loveMonster.loveId1);
			LOVE_MONSTER_MAP1.remove(loveMonster.loveId2);
		}
	}

	@Override
	public int cleanCache(long actorId) {
		LOVE_MONSTER_MAP1.remove(actorId);
		LOVE_MONSTER_MAP2.remove(actorId);
		return LOVE_MONSTER_MAP1.size();
	}

}
