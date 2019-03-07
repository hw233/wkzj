package com.jtang.gameserver.module.hole.dao.impl;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.HoleNotify;
import com.jtang.gameserver.module.hole.dao.HoleNotifyDao;
import com.jtang.gameserver.module.hole.model.HoleNotifyVO;

@Component
public class HoleNotifyDaoImpl implements HoleNotifyDao, CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;

	private static ConcurrentLinkedHashMap<Long, HoleNotify> HOLE_NOTIFY_MAP = new ConcurrentLinkedHashMap.Builder<Long, HoleNotify>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	@Override
	public HoleNotify get(long actorId) {
		return getAndVolidate(actorId);
	}
	
	
	private HoleNotify getAndVolidate(long actorId) {
		HoleNotify holeNotify = null;
		if (HOLE_NOTIFY_MAP.containsKey(actorId)) {
			holeNotify = HOLE_NOTIFY_MAP.get(actorId);
		} else {
			holeNotify = jdbc.get(HoleNotify.class, actorId);
			if (holeNotify == null) {
				holeNotify = HoleNotify.valueOf(actorId);
			}
			HOLE_NOTIFY_MAP.put(actorId, holeNotify);
		}
		Iterator<HoleNotifyVO> it = holeNotify.getList().iterator();
		boolean flag = false;
		while (it.hasNext()) {
			HoleNotifyVO vo = it.next();
			if (vo.enable() == false) {
				it.remove();
				flag = true;
			}
		}
		
		if (flag) {
			update(holeNotify);
		}
		return holeNotify;
	}

	@Override
	public void update(HoleNotify holeNotify) {
		dbQueue.updateQueue(holeNotify);
	}
	@Override
	public int cleanCache(long actorId) {
		HOLE_NOTIFY_MAP.remove(actorId);
		return HOLE_NOTIFY_MAP.size();
	}

}
