package com.jtang.worldserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.worldserver.dataconfig.model.CrossBattleRankConfig;

@Component
public class CrossBattleRankService extends ServiceAdapter {
	
//	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	/**
	 * key：powerRank
	 */
	private static Map<Integer, CrossBattleRankConfig> map = new HashMap<Integer, CrossBattleRankConfig>();

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public void initialize() {
		List<CrossBattleRankConfig> list = dataConfig.listAll(this, CrossBattleRankConfig.class);
		
		for (CrossBattleRankConfig crossBattleRankConfig : list) {
			map.put(crossBattleRankConfig.getPowerRank(), crossBattleRankConfig);
		}
	}
	
	public static CrossBattleRankConfig get(int powerRank) {
		return map.get(powerRank);
	}
	
	

}
