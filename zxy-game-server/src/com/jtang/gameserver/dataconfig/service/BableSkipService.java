package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.BableSkipConfig;

/**
 * 登天塔跳层配置服务
 * @author ludd
 *
 */
@Component
public class BableSkipService extends ServiceAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BableSkipService.class);
	private static List<BableSkipConfig> map = new ArrayList<>();

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public void initialize() {
		List<BableSkipConfig> list = dataConfig.listAll(this, BableSkipConfig.class);
		map.addAll(list);
	}
	
	public static BableSkipConfig get(int bableId, int consumeGoodsId) {
		for (BableSkipConfig cfg : map) {
			if (cfg.getBableId() == bableId && cfg.getConsumeGoodsId() == consumeGoodsId) {
				return cfg;
			}
		}
		LOGGER.error(String.format("不存在跳层配置, bableId:[%s]", bableId));
		return null;
	}
	
	
	

}
