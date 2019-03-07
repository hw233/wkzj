package com.jtang.worldserver.module.crossbattle.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.BaseJdbcTemplate;
import com.jtang.core.db.DBQueue;
import com.jtang.worldserver.dbproxy.entity.CrossBattleActor;
import com.jtang.worldserver.module.crossbattle.dao.CrossBattleActorDao;

@Component
public class CrossBattleActorDaoImpl implements CrossBattleActorDao {
//	private static final Logger LOGGER = LoggerFactory.getLogger(CrossBattleActorDaoImpl.class);

	@Autowired
	BaseJdbcTemplate jdbc;
	@Autowired
	private DBQueue dbQueue;

	private static ConcurrentLinkedHashMap<Long, CrossBattleActor> CROSS_BATTLE_ACTOR_MAP = new ConcurrentLinkedHashMap.Builder<Long, CrossBattleActor>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public CrossBattleActor getCrossBattleActor(long actorId, int serverId) {
		if (CROSS_BATTLE_ACTOR_MAP.containsKey(actorId)) {
			return CROSS_BATTLE_ACTOR_MAP.get(actorId);
		}
		CrossBattleActor crossBattleActor = jdbc.get(CrossBattleActor.class, actorId);
		if (crossBattleActor == null) {
			crossBattleActor = CrossBattleActor.valueOf(actorId, serverId);
		}
		CROSS_BATTLE_ACTOR_MAP.put(actorId, crossBattleActor);
		return crossBattleActor;
	}

	@Override
	public void update(CrossBattleActor crossBattleActor) {
		dbQueue.updateQueue(crossBattleActor);
	}
	@Override
	public void update(List<CrossBattleActor> crossBattleActors) {
		for (CrossBattleActor crossBattleActor : crossBattleActors) {
			CROSS_BATTLE_ACTOR_MAP.put(crossBattleActor.getPkId(), crossBattleActor);
			dbQueue.updateQueue(crossBattleActor);
		}
	}

}
