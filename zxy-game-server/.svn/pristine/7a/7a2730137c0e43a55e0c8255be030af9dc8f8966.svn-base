package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.HeroBookConfig;

/**
 * Hero配置服务读取类
 * @author ludd
 *
 */
@Component
public class HeroBookService extends ServiceAdapter {

	/**
	 * 仙人图鉴配置列表
	 */
	private static Map<Integer, HeroBookConfig> HERO_CONFIG_MAPS = new HashMap<>();
	

	@Override
	public void clear() {
		HERO_CONFIG_MAPS.clear();
	}
	
	@Override
	public void initialize() {
		List<HeroBookConfig> heroList = dataConfig.listAll(this, HeroBookConfig.class);
		for (HeroBookConfig hero : heroList) {
			HERO_CONFIG_MAPS.put(hero.getHeroId(), hero);
		}

	}

	/**
	 * 获取Hero图鉴配置
	 * @param heroId 仙人Id
	 * @return
	 */
	public static HeroBookConfig get(int heroId) {
		if (HERO_CONFIG_MAPS.containsKey(heroId)) {
			return HERO_CONFIG_MAPS.get(heroId);
		}
		return null;
	}

	
}
