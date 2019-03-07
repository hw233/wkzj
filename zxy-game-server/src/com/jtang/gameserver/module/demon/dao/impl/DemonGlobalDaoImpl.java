package com.jtang.gameserver.module.demon.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.DemonGlobal;
import com.jtang.gameserver.module.demon.dao.DemonGlobalDao;
@Component
public class DemonGlobalDaoImpl implements DemonGlobalDao {

	
	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	/**
	 * key:难度
	 */
	private static ConcurrentLinkedHashMap<Long, DemonGlobal> DEMON_MONSTER_MAPS = new ConcurrentLinkedHashMap.Builder<Long, DemonGlobal>().maximumWeightedCapacity(
			1000).build();
	@Override
	public DemonGlobal get(long difficult) {
		if (DEMON_MONSTER_MAPS.containsKey(difficult)) {
			return DEMON_MONSTER_MAPS.get(difficult);
		}

		DemonGlobal demonGlobal = jdbc.get(DemonGlobal.class, difficult);
		if (demonGlobal == null){
			demonGlobal = DemonGlobal.valueOf(difficult);
		}
		DEMON_MONSTER_MAPS.put(difficult, demonGlobal);
		return demonGlobal;
	}

	@Override
	public boolean update(DemonGlobal demonGlobal) {
		dbQueue.updateQueue(demonGlobal);
		return true;
	}

}
