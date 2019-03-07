package com.jtang.gameserver.module.sign.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Sign;
import com.jtang.gameserver.module.sign.dao.SignDao;

@Repository
public class SignDaoImpl implements SignDao, CacheListener {
	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	
	/**
	 * 角色列表.key:actorId
	 */
	private static ConcurrentLinkedHashMap<Long, Sign> SIGN_MAP = new ConcurrentLinkedHashMap.Builder<Long, Sign>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public Sign get(long actorId) {
		if (SIGN_MAP.containsKey(actorId)) {
			return SIGN_MAP.get(actorId);
		}
		Sign sign = jdbc.get(Sign.class, actorId);
		if (sign == null) {
			sign = Sign.valueOf(actorId);
		}
		SIGN_MAP.put(actorId, sign);
		return sign;
	}

	@Override
	public void update(Sign sign) {
		sign.operationTime = TimeUtils.getNow();
		dbQueue.updateQueue(sign);
	}

	@Override
	public int cleanCache(long actorId) {
		SIGN_MAP.remove(actorId);
		return SIGN_MAP.size();
	}

}
