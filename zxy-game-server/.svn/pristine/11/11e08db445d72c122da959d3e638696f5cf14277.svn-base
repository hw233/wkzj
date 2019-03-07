package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.MapConfig;

/**
 * 地图模块配置服务类
 * @author vinceruan
 *
 */
@Component
public class MapService extends ServiceAdapter {
	/**
	 * 地图配置,格式是Map<MapId, MapConfig>
	 */
	static Map<Integer, MapConfig> MAP_CONFIG = new HashMap<Integer, MapConfig>();
	
	@Override
	public void clear() {
		MAP_CONFIG.clear();
	}
	
	@Override
	public void initialize() {
		List<MapConfig> mapList = dataConfig.listAll(this, MapConfig.class);
		
		for (MapConfig conf : mapList) {
			MAP_CONFIG.put(conf.getMapId(), conf);
		}
	}

	public static MapConfig get(int mapId) {
		return MAP_CONFIG.get(mapId);
	}
}
