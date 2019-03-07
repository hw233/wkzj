package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.AchievementConfig;

@Component
public class AchievementService extends ServiceAdapter {
	/**
	 * key-achieveId
	 */
	private static Map<Integer, AchievementConfig> CONFIG_MAP = new HashMap<>();
	private static Map<Integer, List<AchievementConfig>> CONFIG_MAP_ON_TYPE = new HashMap<>();
	
	@Override
	public void clear() {
		CONFIG_MAP.clear();
		CONFIG_MAP_ON_TYPE.clear();
	}
	
	
	@Override
	public void initialize() {
		List<AchievementConfig> allAchievement = dataConfig.listAll(this, AchievementConfig.class);
		for (AchievementConfig config : allAchievement) {
			if (!CONFIG_MAP.containsKey(config.getAchieveId())) {
				CONFIG_MAP.put(config.getAchieveId(), config);
			}
		}
		for (AchievementConfig config : allAchievement) {
			if (CONFIG_MAP_ON_TYPE.containsKey(config.getAchieveType())) {
				CONFIG_MAP_ON_TYPE.get(config.getAchieveType()).add(config);
			} else {
				List<AchievementConfig> configList = new ArrayList<>();
				configList.add(config);
				CONFIG_MAP_ON_TYPE.put(config.getAchieveType(), configList);
			}
		}
	}


	public static List<AchievementConfig> getByType(int achieveType){
		return CONFIG_MAP_ON_TYPE.get(achieveType);
	}

	public static AchievementConfig get(int achieveId) {
		return CONFIG_MAP.get(achieveId);
	}
}
