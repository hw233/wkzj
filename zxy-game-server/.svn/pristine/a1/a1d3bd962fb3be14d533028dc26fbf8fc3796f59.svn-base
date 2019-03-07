package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.FavorTriggerConfig;
@Component
public class FavorTriggerService extends ServiceAdapter {

	private static Map<Integer, FavorTriggerConfig> CONFIG_MAP = new HashMap<>();
	
	@Override
	public void clear() {
		CONFIG_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<FavorTriggerConfig> list = dataConfig.listAll(this, FavorTriggerConfig.class);
		for (FavorTriggerConfig favorTriggerConfig : list) {
			CONFIG_MAP.put(favorTriggerConfig.type, favorTriggerConfig);
		}
	}
	
	public static FavorTriggerConfig get(int type){
		return CONFIG_MAP.get(type);
	}

}
