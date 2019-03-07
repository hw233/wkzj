package com.jtang.gameserver.module.extapp.vipbox.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.VipBox;
import com.jtang.gameserver.module.extapp.vipbox.dao.VipBoxDao;

@Component
public class VipBoxDaoImpl implements VipBoxDao,CacheListener {
	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, VipBox> VIP_BOX_MAP = new ConcurrentLinkedHashMap.Builder<Long, VipBox>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public VipBox get(long actorId) {
		if(VIP_BOX_MAP.containsKey(actorId)){
			return VIP_BOX_MAP.get(actorId);
		}
		VipBox vipBox = jdbc.get(VipBox.class, actorId);
		if(vipBox == null){
			vipBox = VipBox.valueOf(actorId);
		}
		VIP_BOX_MAP.put(actorId, vipBox);
		return vipBox;
	}

	@Override
	public int cleanCache(long actorId) {
		VIP_BOX_MAP.remove(actorId);
		return VIP_BOX_MAP.size();
	}

	@Override
	public void update(VipBox vipBox) {
		dbQueue.updateQueue(vipBox);
	}


}
