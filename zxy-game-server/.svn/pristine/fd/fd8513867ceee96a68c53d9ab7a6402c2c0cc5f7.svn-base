package com.jtang.gameserver.module.extapp.beast.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.BeastGlobal;
import com.jtang.gameserver.module.extapp.beast.dao.BeastGlobalDao;
@Repository
public class BeastGlobalDaoImpl implements BeastGlobalDao {

	
	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	/**
	 * key:配置ID
	 */
	private static ConcurrentLinkedHashMap<Integer, BeastGlobal> BEAST_MONSTER_MAPS = new ConcurrentLinkedHashMap.Builder<Integer, BeastGlobal>().maximumWeightedCapacity(
			1000).build();
	@Override
	public BeastGlobal get(int configId) {
		if (BEAST_MONSTER_MAPS.containsKey(configId)) {
			return BEAST_MONSTER_MAPS.get(configId);
		}

		BeastGlobal beastGlobal = jdbc.get(BeastGlobal.class, configId);
		if (beastGlobal == null){
			beastGlobal = BeastGlobal.valueOf(configId);
		}
		BEAST_MONSTER_MAPS.put(configId, beastGlobal);
		return beastGlobal;
	}

	@Override
	public boolean update(BeastGlobal beastGlobal) {
		dbQueue.updateQueue(beastGlobal);
		return true;
	}

}
