package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.HeroBookRewardConfig;

/**
 * Herobook配置服务读取类
 * @author ludd
 *
 */
@Component
public class HeroBookRewardService extends ServiceAdapter {

	/**
	 * 仙人图鉴奖励配置列表
	 */
	private static Map<Integer, HeroBookRewardConfig> HERO_CONFIG_MAPS = new HashMap<>();
	

	@Override
	public void clear() {
		HERO_CONFIG_MAPS.clear();
	}
	
	@Override
	public void initialize() {
		List<HeroBookRewardConfig> list = dataConfig.listAll(this, HeroBookRewardConfig.class);
		for (HeroBookRewardConfig cfg : list) {
			HERO_CONFIG_MAPS.put(cfg.getOrderId(), cfg);
		}

	}

	/**
	 * 获取Hero图鉴奖励配置
	 * @param heroId 仙人Id
	 * @return
	 */
	public static HeroBookRewardConfig get(int orderId) {
		if (HERO_CONFIG_MAPS.containsKey(orderId)) {
			return HERO_CONFIG_MAPS.get(orderId);
		}
		return null;
	}
	
	/**
	 * 获取开始节点
	 * @return
	 */
	public static Set<Integer> getStartNode() {
		Set<Integer> set = new HashSet<>();
		for (HeroBookRewardConfig heroBookRewardConfig : HERO_CONFIG_MAPS.values()) {
			if (heroBookRewardConfig.getIsStartNode() == 1) {
				set.add(heroBookRewardConfig.getOrderId());
			}
		}
		return set;
	}

	
}
