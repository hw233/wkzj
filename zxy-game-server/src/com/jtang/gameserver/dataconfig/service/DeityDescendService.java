package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.DeityDescendGlobalConfig;
import com.jtang.gameserver.dataconfig.model.DeityDescendHeroConfig;

@Service
public class DeityDescendService extends ServiceAdapter {

	public static DeityDescendGlobalConfig DEITY_DESCEND_GLOBAL_CONFIG = new DeityDescendGlobalConfig();

	public static Map<Integer, DeityDescendHeroConfig> DEITY_DESCEND_HERO_MAP = new HashMap<Integer, DeityDescendHeroConfig>();
	
	public static final int MAX_CHAR_NUM = 4;
	
	private static int startTime = 0;
	
	private static int endTime = 0;
	@Override
	public void clear() {
		DEITY_DESCEND_GLOBAL_CONFIG = new DeityDescendGlobalConfig();
		DEITY_DESCEND_HERO_MAP.clear();
	}

	@Override
	public void initialize() {
		List<DeityDescendGlobalConfig> global = dataConfig.listAll(this, DeityDescendGlobalConfig.class);
		for (DeityDescendGlobalConfig deityDescendGlobalConfig : global) {
			DEITY_DESCEND_GLOBAL_CONFIG = deityDescendGlobalConfig;
		}

		List<DeityDescendHeroConfig> heroList = dataConfig.listAll(this, DeityDescendHeroConfig.class);
		for (DeityDescendHeroConfig deityDescendHeroConfig : heroList) {
			DEITY_DESCEND_HERO_MAP.put(deityDescendHeroConfig.heroId, deityDescendHeroConfig);
		}
		
		Long startDate = DEITY_DESCEND_GLOBAL_CONFIG.startTime.getTime();
		startTime = startDate.intValue();
		Long endDate = DEITY_DESCEND_GLOBAL_CONFIG.endTime.getTime();
		endTime = endDate.intValue();
	}

	public static DeityDescendHeroConfig getDescendHeroConfigByHeroId(int heroId) {
		if (DEITY_DESCEND_HERO_MAP.containsKey(heroId)) {
			return DEITY_DESCEND_HERO_MAP.get(heroId);
		}
		return null;
	}

	public static int getCostByHitCount(int heroId, int hitCount) {
		DeityDescendHeroConfig config = getDescendHeroConfigByHeroId(heroId);
		if (config != null) {
			if (hitCount == 1) {
				return config.price1;
			} else {
				return config.price10;
			}
		}
		return 0;
	}
	
	public static int getConvertNumByHero(int heroId){
		DeityDescendHeroConfig config = getDescendHeroConfigByHeroId(heroId);
		if (config != null) {
			return config.convertNum;
		}
		return 0;
	}

	public static int getHitPorbByHeroIdAndIndex(int heroId, int index){
		if (index >= MAX_CHAR_NUM) return 0;
		DeityDescendHeroConfig config = getDescendHeroConfigByHeroId(heroId);
		if (config != null) {
			return config.hitProbList.get(index);
		}
		return 0;
	}

	public static int getUseNumByHeroIdAndIndex(int heroId, int index){
		if (index >= MAX_CHAR_NUM) return 0;
		DeityDescendHeroConfig config = getDescendHeroConfigByHeroId(heroId);
		if (config != null) {
			return config.hitUseNumList.get(index);
		}
		return 0;
	}
	
	public static int getCurrentHeroId() {
		return DEITY_DESCEND_GLOBAL_CONFIG.currentHeroId;
	}
	
	public static int getStartDate() {
		return startTime;
	}
	
	public static int getEndDate() {
		return endTime;
	}
	
}
