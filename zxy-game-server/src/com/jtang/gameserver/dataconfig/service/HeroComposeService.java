package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.HeroComposeConfig;

/**
 * 仙人合成配置服务类
 * @author 0x737263
 *
 */
@Component
public class HeroComposeService extends ServiceAdapter {

	/**
	 * key:composeStar value:EquipComposeConfig
	 */
	private static Map<Integer,HeroComposeConfig> HERO_COMPOSE_MAPS = new HashMap<>();
	
	@Override
	public void clear() {
		HERO_COMPOSE_MAPS.clear();
	}
	
	@Override
	public void initialize() {
		List<HeroComposeConfig> list = dataConfig.listAll(this, HeroComposeConfig.class);
		for (HeroComposeConfig config : list) {
			HERO_COMPOSE_MAPS.put(config.getComposeStar(), config);
		}
	}
	
	/**
	 * 获取一条仙人合成配置
	 * @param composeStar	合成星级
	 * @return
	 */
	public static HeroComposeConfig get(int composeStar) {
		return  HERO_COMPOSE_MAPS.get(composeStar);
	}
	
	
	/**
	 * 随机仙人id
	 * @param composeStar	合成的星级
	 * @param useTicket		是否使用点券
	 * @return
	 */
	public static int randHero(int composeStar, boolean useTicket) {
		HeroComposeConfig config = get(composeStar);
		if (config == null) {
			return 0;
		}

		int randMax = config.getBasePercent();
		boolean isHit = false;
		if (useTicket) {
			randMax = config.getUseTicketPercent();
			isHit = true;//使用点券100%命中
		} else {
			isHit = RandomUtils.is100Hit(randMax);
		}

		if (isHit == false) {
			return 0;// 未命中.
		}

		Map<Integer, Integer> heroMaps = config.getHeroMaps(); // .getEquipMaps();
		Integer id = RandomUtils.randomHit(100, heroMaps);
		if(id == null){
			return 0;
		}
		return id;
	}
	
}
