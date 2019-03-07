package com.jtang.gameserver.module.ally.dao.impl;

import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.gameserver.module.ally.dao.AllyCoordinateDao;
import com.jtang.gameserver.module.ally.model.CoordinateVO;

/**
 * 盟友坐标信息 
 * @author 0x737263
 *
 */
@Component
public class AllyCoordinateDaoImpl implements AllyCoordinateDao, CacheListener {

	/**
	 * key:actorId value:CoordinateVO
	 */
	private static ConcurrentMap<Long, CoordinateVO> ALLY_COORDINATE_MAP = new ConcurrentLinkedHashMap.Builder<Long, CoordinateVO>()
			.maximumWeightedCapacity(Short.MAX_VALUE).build();
	
	@Override
	public CoordinateVO get(long actorId) {
		if (ALLY_COORDINATE_MAP.containsKey(actorId))
			return ALLY_COORDINATE_MAP.get(actorId);
		return null;
	}

	@Override
	public void add(long actorId, CoordinateVO coordianteVO) {
		if (!ALLY_COORDINATE_MAP.containsKey(actorId)) {
			ALLY_COORDINATE_MAP.put(actorId, coordianteVO);
		}
	}

	@Override
	public void update(long actorId, double longitude, double latitude, double levelError) {
		CoordinateVO coordinate = get(actorId);
		if (coordinate == null) {
			coordinate = CoordinateVO.valueOf(longitude, latitude, levelError);
			ALLY_COORDINATE_MAP.put(actorId, coordinate);
			return;
		}
		coordinate.levelError = levelError;
		coordinate.latitude = latitude;
		coordinate.longitude = longitude;
	}

	@Override
	public int cleanCache(long actorId) {
		ALLY_COORDINATE_MAP.remove(actorId);
		return ALLY_COORDINATE_MAP.size();
	}

}
