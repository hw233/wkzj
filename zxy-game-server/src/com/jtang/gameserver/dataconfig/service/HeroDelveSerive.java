package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.HeroDelveConfig;

/**
 *  仙人潜修服务类
 * @author ludd
 *
 */
@Component
public class HeroDelveSerive extends ServiceAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(HeroDelveSerive.class);
	private static Map<Integer, HeroDelveConfig> HERO_DELVE_CONFIG_MAPS = new HashMap<Integer, HeroDelveConfig>();

	@Override
	public void clear() {
		HERO_DELVE_CONFIG_MAPS.clear();
	}
	
	@Override
	public void initialize() {
		List<HeroDelveConfig> heroUpgradeList = dataConfig.listAll(this, HeroDelveConfig.class);
		for (HeroDelveConfig heroDelveConfig : heroUpgradeList) {
			HERO_DELVE_CONFIG_MAPS.put(heroDelveConfig.getHeroId(), heroDelveConfig);
		}		
	}
	
	
	/**
	 * 获取仙人潜修配置
	 * @param heroId
	 * @return
	 */
	public static HeroDelveConfig get(int heroId) {
		if (HERO_DELVE_CONFIG_MAPS.containsKey(heroId)){
			return HERO_DELVE_CONFIG_MAPS.get(heroId);
		}
		LOGGER.error(String.format("仙人潜修配置不存在, heroId:[%s]", heroId));
		return null;
	}

}
