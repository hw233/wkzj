package com.jtang.gameserver.module.icon.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Icon;
import com.jtang.gameserver.module.icon.dao.IconDao;

@Component
public class IconDaoImpl implements IconDao,CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, Icon> ICON_MAP = new ConcurrentLinkedHashMap.Builder<Long, Icon>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Icon get(long actorId) {
		if(ICON_MAP.containsKey(actorId)){
			return ICON_MAP.get(actorId);
		}
		Icon icon = jdbc.get(Icon.class, actorId);
		if(icon == null){
			icon = Icon.valueOf(actorId);
		}
		ICON_MAP.put(actorId, icon);
		return icon;
	}
	
	@Override
	public void update(Icon icon){
		dbQueue.updateQueue(icon);
	}

	@Override
	public int cleanCache(long actorId) {
		ICON_MAP.remove(actorId);
		return ICON_MAP.size();
	}

}
