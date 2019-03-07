package com.jtang.gameserver.module.extapp.rechargeapp.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.RechargeApp;
import com.jtang.gameserver.module.extapp.rechargeapp.dao.RechargeAppDao;

@Component
public class RechargeAppDaoImpl implements RechargeAppDao,CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, RechargeApp> RECHARGE_APP_MAP = new ConcurrentLinkedHashMap.Builder<Long, RechargeApp>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public RechargeApp get(long actorId) {
		if(RECHARGE_APP_MAP.containsKey(actorId)){
			return RECHARGE_APP_MAP.get(actorId);
		}
		RechargeApp rechargeApp = jdbc.get(RechargeApp.class, actorId);
		if(rechargeApp == null){
			rechargeApp = RechargeApp.valueOf(actorId);
		}
		RECHARGE_APP_MAP.put(actorId,rechargeApp);
		return rechargeApp;
	}

	@Override
	public void update(RechargeApp rechargeApp) {
		dbQueue.updateQueue(rechargeApp);
	}

	@Override
	public int cleanCache(long actorId) {
		RECHARGE_APP_MAP.remove(actorId);
		return RECHARGE_APP_MAP.size();
	}
	
	

}
