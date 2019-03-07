package com.jtang.worldserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.worldserver.dataconfig.model.CrossBattlePointConfig;

@Component
public class CrossBattlePointService extends ServiceAdapter {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(CrossBattlePointService.class);
	/**
	 * key：powerRank
	 */
	private static Map<Integer, CrossBattlePointConfig> map = new HashMap<Integer, CrossBattlePointConfig>();

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public void initialize() {
		List<CrossBattlePointConfig> list = dataConfig.listAll(this, CrossBattlePointConfig.class);
		
		for (CrossBattlePointConfig crossBattlePointConfig : list) {
			map.put(crossBattlePointConfig.getHurtRank(), crossBattlePointConfig);
		}
	}
	
	public static CrossBattlePointConfig get(int hurtRank) {
		if (map.containsKey(hurtRank)) {
			return map.get(hurtRank);
		}
		LOGGER.error(String.format("伤害排名贡献点配置不存在, hurtRank:[%s]", hurtRank));
		return null;
	}
	
	

}
