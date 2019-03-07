package com.jtang.gameserver.module.extapp.randomreward.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.RandomReward;
import com.jtang.gameserver.module.extapp.randomreward.dao.RandomRewardDao;

@Component
public class RandomRewardDaoImpl implements RandomRewardDao,CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, RandomReward> RANDOM_REWARD_MAP = new ConcurrentLinkedHashMap.Builder<Long, RandomReward>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	
	@Override
	public RandomReward get(long actorId) {
		if(RANDOM_REWARD_MAP.containsKey(actorId)){
			return RANDOM_REWARD_MAP.get(actorId);
		}
		RandomReward randomReward = jdbc.get(RandomReward.class, actorId);
		if(randomReward == null){
			randomReward = RandomReward.valueOf(actorId);
		}
		RANDOM_REWARD_MAP.put(actorId, randomReward);
		return randomReward;
	}


	@Override
	public void update(RandomReward randomReward) {
		dbQueue.updateQueue(randomReward);
	}


	@Override
	public int cleanCache(long actorId) {
		RANDOM_REWARD_MAP.remove(actorId);
		return RANDOM_REWARD_MAP.size();
	}

}
