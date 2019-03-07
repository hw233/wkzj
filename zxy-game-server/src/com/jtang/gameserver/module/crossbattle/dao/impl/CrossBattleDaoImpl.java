package com.jtang.gameserver.module.crossbattle.dao.impl;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.CrossBattleActorRewardFlag;
import com.jtang.gameserver.dbproxy.entity.CrossBattleReward;
import com.jtang.gameserver.module.crossbattle.dao.CrossBattleDao;

@Component
public class CrossBattleDaoImpl implements CrossBattleDao,CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, CrossBattleActorRewardFlag> ACTOR_REWARD_MAP = new ConcurrentLinkedHashMap.Builder<Long, CrossBattleActorRewardFlag>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	private CrossBattleReward crossBattleReward = null;
	
	@Override
	public CrossBattleReward getReward() {
		if(crossBattleReward != null){
			return crossBattleReward;
		}
		CrossBattleReward reward = jdbc.getFirst(CrossBattleReward.class,new LinkedHashMap<String,Object>());
		this.crossBattleReward = reward;
		return reward;
	}

	@Override
	public CrossBattleActorRewardFlag get(long actorId) {
		if(ACTOR_REWARD_MAP.containsKey(actorId)){
			return ACTOR_REWARD_MAP.get(actorId);
		}
		CrossBattleActorRewardFlag actorRewardFlag = jdbc.get(CrossBattleActorRewardFlag.class, actorId);
		if(actorRewardFlag == null){
			actorRewardFlag = CrossBattleActorRewardFlag.valueOf(actorId);
		}
		ACTOR_REWARD_MAP.put(actorId, actorRewardFlag);
		return actorRewardFlag;
	}

	@Override
	public void update(CrossBattleActorRewardFlag actorReward) {
		dbQueue.updateQueue(actorReward);
	}

	@Override
	public void update(String rewardObjects) {
		long time = System.currentTimeMillis();
		CrossBattleReward crossBattleReward = getReward();
		if(crossBattleReward == null){
			crossBattleReward = CrossBattleReward.valueOf(rewardObjects,time);
		}else{
			crossBattleReward.reward = rewardObjects;
			crossBattleReward.rewardTime = time;
		}
		this.crossBattleReward = crossBattleReward;
		dbQueue.updateQueue(crossBattleReward);
	}
	
	@Override
	public int cleanCache(long actorId) {
		ACTOR_REWARD_MAP.remove(actorId);
		return ACTOR_REWARD_MAP.size();
	}
	
}
