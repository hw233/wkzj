package com.jtang.worldserver.module.crossbattle.dao.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.BaseJdbcTemplate;
import com.jtang.core.db.DBQueue;
import com.jtang.worldserver.dbproxy.entity.CrossBattleHurtRank;
import com.jtang.worldserver.module.crossbattle.dao.CrossBattleHurtRankDao;

@Component
public class CrossBattleHurtRankDaoImpl implements CrossBattleHurtRankDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrossBattleHurtRankDaoImpl.class);
	
	@Autowired
	BaseJdbcTemplate jdbc;
	
	@Autowired
	private DBQueue dbQueue;

	private static ConcurrentLinkedHashMap<Long, CrossBattleHurtRank> CROSS_BATTLE_HURT_RANK_MAP = new ConcurrentLinkedHashMap.Builder<Long, CrossBattleHurtRank>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public CrossBattleHurtRank getCrossBattleHurtRank(long actorId, int serverId) {
		if (CROSS_BATTLE_HURT_RANK_MAP.containsKey(actorId)) {
			return CROSS_BATTLE_HURT_RANK_MAP.get(actorId);
		}
		CrossBattleHurtRank crossBattleHurtRank = jdbc.get(CrossBattleHurtRank.class, actorId);
		if (crossBattleHurtRank == null) {
			crossBattleHurtRank = CrossBattleHurtRank.valueOf(actorId);
		}
		CROSS_BATTLE_HURT_RANK_MAP.put(actorId, crossBattleHurtRank);
		return crossBattleHurtRank;
	}
	
	@Override
	public List<CrossBattleHurtRank> getCrossBattleServerHurtRank(int serverId) {
		LinkedHashMap<String, Object> hurtRankMap = new LinkedHashMap<String, Object>();
		hurtRankMap.put("serverId", serverId);
		List<CrossBattleHurtRank> serverRankList = jdbc.getList(CrossBattleHurtRank.class, hurtRankMap);
		return serverRankList;
	}

	@Override
	public void update(CrossBattleHurtRank crossBattleHurtRank) {
		dbQueue.updateQueue(crossBattleHurtRank);
	}
	
	@Override
	public void update(List<CrossBattleHurtRank> crossBattleHurtRanks) {
		synchronized (CROSS_BATTLE_HURT_RANK_MAP) {
			CROSS_BATTLE_HURT_RANK_MAP.clear();
			try {
				jdbc.update("delete from crossBattleHurtRank");
			} catch (DataAccessException e) {
				LOGGER.error("{}", e);
			}
			for (CrossBattleHurtRank crossBattleHurtRank : crossBattleHurtRanks) {
				CROSS_BATTLE_HURT_RANK_MAP.put(crossBattleHurtRank.getPkId(), crossBattleHurtRank);
				dbQueue.updateQueue(crossBattleHurtRank);
			}
		}
	}

}
