package com.jtang.gameserver.module.extapp.plant.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Plant;
import com.jtang.gameserver.module.extapp.plant.dao.PlantDao;

@Component
public class PlantDaoImpl implements PlantDao,CacheListener {

	private static ConcurrentLinkedHashMap<Long, Plant> PLANT_MAP = new ConcurrentLinkedHashMap.Builder<Long, Plant>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	@Override
	public Plant get(long actorId) {
		if(PLANT_MAP.containsKey(actorId)){
			return PLANT_MAP.get(actorId);
		}
		Plant plant = jdbc.get(Plant.class, actorId);
		if(plant == null){
			plant = Plant.valueOf(actorId);
		}
		PLANT_MAP.put(actorId, plant);
		return plant;
	}

	@Override
	public void update(Plant plant) {
		dbQueue.updateQueue(plant);
	}

	@Override
	public int cleanCache(long actorId) {
		PLANT_MAP.remove(actorId);
		return PLANT_MAP.size();
	}

}
