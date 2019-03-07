package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.DemonConfig;

/**
 * 集众降魔配置服务
 * @author ludd
 *
 */
@Component
public class DemonService extends ServiceAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DemonService.class);
	
	private static Map<Integer, DemonConfig> MAP = new HashMap<Integer, DemonConfig>(); 
	
	private static int powerRankMax;
	
	/**
	 * 最难难度
	 */
	private static int hardDiffcult = 1;
	
	@Override
	public void clear() {
		MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<DemonConfig> list = dataConfig.listAll(this, DemonConfig.class);
		for (DemonConfig demonConfig : list) {
			MAP.put(demonConfig.getDifficultId(), demonConfig);
			powerRankMax = powerRankMax < demonConfig.getPowerRankMax() ? demonConfig.getPowerRankMax() : powerRankMax;
			int dif = demonConfig.getDifficultId();
			hardDiffcult = hardDiffcult > dif ? dif : hardDiffcult;
		}
	}
	
	public static DemonConfig get(int difficult) {
		if (MAP.containsKey(difficult)) {
			return MAP.get(difficult);
		}
		LOGGER.error(String.format("不存在DemonConfig， difficult:[%s]", difficult));
		return null;
	}
	
	public static int getPowerRankMax() {
		return powerRankMax;
	}
	
	public static DemonConfig getByPowerRank(int powerRank) {
		for (DemonConfig demonConfig : MAP.values()) {
			if (demonConfig.getPowerRankMin() <= powerRank && powerRank <= demonConfig.getPowerRankMax()) {
				return demonConfig;
			}
		}
		LOGGER.error(String.format("不存在DemonConfig， powerRank:[%s]", powerRank));
		return null;
	}
	
	public static int getHardDiffcult() {
		return hardDiffcult;
	}
	
	


}
