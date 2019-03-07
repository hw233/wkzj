package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.ResetHeroEquipConfig;

@Component
public class ResetHeroEquipService extends ServiceAdapter {
	
	private static Map<Integer, ResetHeroEquipConfig> RESET_CFG_MAP = new HashMap<>(); 

	@Override
	public void clear() {
		RESET_CFG_MAP.clear();
	}

	@Override
	public void initialize() {
		List<ResetHeroEquipConfig> list = dataConfig.listAll(this, ResetHeroEquipConfig.class);
		for(ResetHeroEquipConfig reset:list){
			RESET_CFG_MAP.put(reset.type, reset);
		}
	}
	
	public static ResetHeroEquipConfig getConfig(int type){
		return RESET_CFG_MAP.get(type);
	}

}
