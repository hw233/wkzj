package com.jtang.gameserver.module.adventures.vipactivity.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.VipPrivilege;
import com.jtang.gameserver.module.adventures.vipactivity.dao.VipPrivilegeDao;

@Component
public class VipPrivilegeDaoImpl implements VipPrivilegeDao {
	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;

	private static ConcurrentLinkedHashMap<Long, VipPrivilege> VIP_MAP = new ConcurrentLinkedHashMap.Builder<Long, VipPrivilege>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public VipPrivilege get(long actorId) {
		if(VIP_MAP.containsKey(actorId)){
			return VIP_MAP.get(actorId);
		}
		VipPrivilege vipPrivilege = jdbc.get(VipPrivilege.class, actorId);
		if(vipPrivilege == null){
			vipPrivilege=VipPrivilege.valueOf(actorId);
		}
		VIP_MAP.put(actorId, vipPrivilege);
		return vipPrivilege;
	}

	@Override
	public void update(VipPrivilege vipPrivilege) {
		dbQueue.updateQueue(vipPrivilege);
	}

}
