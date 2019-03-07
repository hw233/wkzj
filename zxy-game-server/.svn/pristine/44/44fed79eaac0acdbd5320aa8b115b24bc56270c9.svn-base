package com.jtang.gameserver.module.user.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.ErrorEntityBackup;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Vip;
import com.jtang.gameserver.module.user.dao.VipDao;

@Component
public class VipDaoImpl implements VipDao, CacheListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(VipDaoImpl.class);
	@Autowired
	private IdTableJdbc xJdbcTemplate;
	
	@Autowired
	private ErrorEntityBackup entityBackup;
	
	/**
	 * vip列表.key:actorId value:Vip
	 */
	private static ConcurrentLinkedHashMap<Long, Vip> VIP_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Vip>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Vip get(long actorId) {
		if (VIP_MAPS.containsKey(actorId)) {
			return VIP_MAPS.get(actorId);
		}

		Vip vip = xJdbcTemplate.get(Vip.class, actorId);
		if (vip == null) {
			vip = Vip.valueOf(actorId);
		}
		VIP_MAPS.put(actorId, vip);
		return vip;
	}

	@Override
	public boolean updata(Vip vip) {
		try {
			xJdbcTemplate.update(vip);
			return true;
		} catch (Exception e) {
			String tableName = "vip";
			LOGGER.error(String.format("save db error. actorId:%s, tableName:[%s], entity drop.", vip.getPkId(), tableName), e);
			entityBackup.write(vip, tableName);
			return false;
		}
	}
	
	@Override
	public int cleanCache(long actorId) {
		VIP_MAPS.remove(actorId);
		return VIP_MAPS.size();
	}

}
