package com.jtang.gameserver.module.hole.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Hole;
import com.jtang.gameserver.module.hole.dao.HoleDao;
import com.jtang.gameserver.module.hole.type.OpenType;

@Component
public class HoleDaoImpl implements HoleDao, CacheListener {

	@Autowired
	IdTableJdbc jdbc;
//	@Autowired
//	DBQueue dbQueue;

	private static ConcurrentLinkedHashMap<Long, List<Hole>> HOLE_MAP = new ConcurrentLinkedHashMap.Builder<Long, List<Hole>>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public List<Hole> getHoles(long actorId) {
		List<Hole> holes = null;
		if (HOLE_MAP.containsKey(actorId)) {
			holes = HOLE_MAP.get(actorId);
			if (holes.size() > 0) {
				cleanHole(holes);
				return holes;
			}
		}
		LinkedHashMap<String, Object> holeMap = new LinkedHashMap<String, Object>();
		holeMap.put("actorId", actorId);
		holes = jdbc.getList(Hole.class, holeMap);
		if (holes == null || holes.isEmpty()) {
			return null;
		}
		HOLE_MAP.put(actorId, holes);
		cleanHole(holes);
		return holes;
	}

	@Override
	public Hole getHole(long id, long actorId) {
		List<Hole> holes = getHoles(actorId);
		if (holes != null) {
			for (Hole hole : holes) {
				if (hole.getPkId() == id) {
					return hole;
				}
			}
		}
		return null;
	}

	@Override
	public void updateHole(Hole hole) {
		jdbc.update(hole);
	}

	@Override
	public long addHole(Hole hole) {
		if (HOLE_MAP.containsKey(hole.actorId)) {
			HOLE_MAP.get(hole.actorId).add(hole);
		} else {
			List<Hole> list = new ArrayList<Hole>();
			list.add(hole);
			HOLE_MAP.put(hole.actorId, list);
		}
		return jdbc.saveAndIncreasePK(hole);
	}

	@Override
	public Hole getHole(long actorId) {
		List<Hole> holeList = getHoles(actorId);
		if (holeList == null) {
			return null;
		}
		
		Iterator<Hole> it = holeList.iterator();
		while (it.hasNext()) {
			Hole hole = it.next();
			if (hole.aviable() == false) {
				it.remove();
				jdbc.delete(hole);
				continue;
			}
			if (hole.type == OpenType.ACTOR.getCode()) {
				return hole;
			}
		}
		return null;
	}

	@Override
	public Hole getHole(long acceptActor, long actorId, int holeId) {
		List<Hole> holes = getHoles(actorId);
		if (holes == null) {
			return null;
		}
		Iterator<Hole> it = holes.iterator();
		while (it.hasNext()) {
			Hole hole = it.next();
			if (hole.aviable() == false) {
				it.remove();
				jdbc.delete(hole);
				continue;
			}
			if (hole.ally == actorId && holeId == hole.holeId) {
				return hole;
			}
		}
		LinkedHashMap<String, Object> holeMap = new LinkedHashMap<String, Object>();
		holeMap.put("actorId", actorId);
		holeMap.put("ally", acceptActor);
		holeMap.put("holeId", holeId);
		holes = jdbc.getList(Hole.class, holeMap);
		if (holes == null||holes.isEmpty()) {
			return null;
		}
		Hole hole = holes.get(0);
		return hole;
	}
	
	@Override
	public int cleanCache(long actorId) {
		HOLE_MAP.remove(actorId);
		return HOLE_MAP.size();
	}
	
	private List<Hole> cleanHole(List<Hole> holeList){
		Iterator<Hole> it = holeList.iterator();
		while (it.hasNext()) {
			Hole hole = it.next();
			if (hole.aviable() == false) {
				it.remove();
				jdbc.delete(hole);
				continue;
			}
		}
		return holeList;
	}

}
