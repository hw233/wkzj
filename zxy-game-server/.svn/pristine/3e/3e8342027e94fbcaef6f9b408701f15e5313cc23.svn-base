package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.SupplyConfig;

@Component
public class SupplyService extends ServiceAdapter {

	static Map<Integer, SupplyConfig> SUPPLY_CONFIG_MAP = new HashMap<Integer, SupplyConfig>();

	@Override
	public void clear() {
		SUPPLY_CONFIG_MAP.clear();
	}

	@Override
	public void initialize() {
		List<SupplyConfig> list = dataConfig.listAll(this, SupplyConfig.class);
		for (SupplyConfig config : list) {
			SUPPLY_CONFIG_MAP.put(config.id, config);
		}
	}
	
	public static Map<Integer,SupplyConfig> getAll(){
		return SUPPLY_CONFIG_MAP;
	}
	
	public static SupplyConfig get(int rewardId){
		return SUPPLY_CONFIG_MAP.get(rewardId);
	}

}
