package com.jtang.worldserver.module.crossbattle.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.BaseJdbcTemplate;
import com.jtang.worldserver.dbproxy.entity.CrossBattle;
import com.jtang.worldserver.module.crossbattle.dao.CrossBattleDao;

@Component
public class CrossBattleDaoImpl implements CrossBattleDao {

	@Autowired
	BaseJdbcTemplate jdbc;

	private static ConcurrentLinkedHashMap<Integer, CrossBattle> CROSS_BATTLE_MAP = new ConcurrentLinkedHashMap.Builder<Integer, CrossBattle>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@PostConstruct
	private void init() {
		List<CrossBattle> list = jdbc.getList(CrossBattle.class);
		for (CrossBattle crossBattle : list) {
			if (CROSS_BATTLE_MAP.containsKey(crossBattle.getPkId()) == false) {
				CROSS_BATTLE_MAP.put(crossBattle.getPkId(), crossBattle);
			}
		}
	}

	@Override
	public CrossBattle getCrossBattle(int serverId) {
		if (CROSS_BATTLE_MAP.containsKey(serverId)) {
			return CROSS_BATTLE_MAP.get(serverId);
		}
		CrossBattle crossBattle = jdbc.get(CrossBattle.class, serverId);
		if (crossBattle == null) {
			crossBattle = CrossBattle.valueOf(serverId);
		}
		CROSS_BATTLE_MAP.put(serverId, crossBattle);
		return crossBattle;
	}

	@Override
	public void update(CrossBattle crossBattle) {
		jdbc.update(crossBattle);
	}
	
	@Override
	public List<CrossBattle> getList() {
		List<CrossBattle> list = new ArrayList<>();
		list.addAll(CROSS_BATTLE_MAP.values());
		Collections.sort(list, new Comparator<CrossBattle>() {

			@Override
			public int compare(CrossBattle o1, CrossBattle o2) {
				if (o1.score > o2.score){
					return -1;
				} else if (o1.score < o2.score) {
					return 1;
				}
				return 0;
			}
		});
		return list;
	}
	
	@Override
	public void update(List<CrossBattle> crossBattles) {
		synchronized (CROSS_BATTLE_MAP) {
			for (CrossBattle crossBattle : crossBattles) {
				CROSS_BATTLE_MAP.put(crossBattle.getPkId(), crossBattle);
				jdbc.update(crossBattle);
			}
		}
	}
	
	@Override
	public void deleteLast() {
		CROSS_BATTLE_MAP.clear();
		jdbc.update("delete from crossBattle");
	}

}
