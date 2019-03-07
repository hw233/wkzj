package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.HeroUseGoodsConfig;

/**
 * 吸灵室使用物品服务类
 * @author 0x737263
 *
 */
@Component
public class HeroUseGoodsService extends ServiceAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(HeroUseGoodsService.class);

	private static Map<Integer, HeroUseGoodsConfig> configs = new HashMap<>();
	@Override
	public void clear() {
		configs.clear();
	}

	@Override
	public void initialize() {
		List<HeroUseGoodsConfig> list = dataConfig.listAll(this, HeroUseGoodsConfig.class);
		
		for (HeroUseGoodsConfig heroUseGoodsConfig : list) {
			configs.put(heroUseGoodsConfig.getGoodsId(), heroUseGoodsConfig);
		}
	}
	
	public static HeroUseGoodsConfig get(int goodsId) {
		if (configs.containsKey(goodsId)){
			return configs.get(goodsId);
		}
		LOGGER.error(String.format("不存在使用物品加经验的配置, goodsId:[%s]", goodsId));
		return null;
	}
	
	public static boolean contains(int goodsId) {
		return configs.containsKey(goodsId);
	}
	
}
