package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.DemonScoreConfig;

/**
 * 集众降魔积分服务
 * @author ludd
 *
 */
@Component
public class DemonScoreService extends ServiceAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DemonScoreService.class);
	
	/**
	 * key:难度
	 */
	private static Map<Integer, List<DemonScoreConfig>> MAP = new HashMap<>(); 
	
	@Override
	public void clear() {
		MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<DemonScoreConfig> list = dataConfig.listAll(this, DemonScoreConfig.class);
		for (DemonScoreConfig demonSocreConfig : list) {
			List<DemonScoreConfig> array = null;
			if (MAP.containsKey(demonSocreConfig.getDifficultId())) {
				array = MAP.get(demonSocreConfig.getDifficultId());
			} else {
				array = new ArrayList<>();
				MAP.put(demonSocreConfig.getDifficultId(), array);
			}
			array.add(demonSocreConfig);
		}
	}
	
	public static DemonScoreConfig getDemonSocreConfig(int difficult, int featsRank) {
		if (MAP.containsKey(difficult) == false) {
			LOGGER.error(String.format("不存在难度:[%s]", difficult));
			return null;
		}
		List<DemonScoreConfig> list = MAP.get(difficult);
		
		for (DemonScoreConfig demonSocreConfig : list) {
			if (demonSocreConfig.getFeatsRankMin() <= featsRank && featsRank <= demonSocreConfig.getFeatsRankMax()) {
				return demonSocreConfig;
			}
		}
		LOGGER.error(String.format("不存在难度:[%s], 功勋排名:[%s]", difficult, featsRank));
		return null;
	}
	
	


}
