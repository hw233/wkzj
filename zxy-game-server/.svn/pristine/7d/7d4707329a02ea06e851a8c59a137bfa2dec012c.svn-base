package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.VampiirStarConfig;

@Component
public class VampiirStarService extends ServiceAdapter{

private static final Logger LOGGER = LoggerFactory.getLogger(DelveService.class);
	
	private static Map<Integer, VampiirStarConfig> VAMPIIR_STAR_MAPS = new HashMap<>(); 

	@Override
	public void clear() {
		VAMPIIR_STAR_MAPS.clear();
	}
	
	@Override
	public void initialize() {
		List<VampiirStarConfig> configs = dataConfig.listAll(this, VampiirStarConfig.class);
		for(VampiirStarConfig doVampiirConfig : configs){
			VAMPIIR_STAR_MAPS.put(doVampiirConfig.getStar(), doVampiirConfig);
		}
	}
	
	public static VampiirStarConfig get(int star){
		if (VAMPIIR_STAR_MAPS.containsKey(star)) {
			return VAMPIIR_STAR_MAPS.get(star);
		}
		LOGGER.error(String.format("VampiirStarConfig缺少配置，star: [%s]", star));
		return null;
	}
}
