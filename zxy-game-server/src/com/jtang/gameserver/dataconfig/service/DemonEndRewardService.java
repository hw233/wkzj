package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.DemonEndRewardConfig;

/**
 * 集众降魔结束奖励配置服务
 * @author ludd
 *
 */
@Component
public class DemonEndRewardService extends ServiceAdapter {
//	private static final Logger LOGGER = LoggerFactory.getLogger(DemonEndRewardService.class);
	
	private static Map<String, DemonEndRewardConfig> MAP = new HashMap<String, DemonEndRewardConfig>(); 
	@Override
	public void clear() {
		MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<DemonEndRewardConfig> list = dataConfig.listAll(this, DemonEndRewardConfig.class);
		for (DemonEndRewardConfig demonEndRewardConfig : list) {
			MAP.put(getKey(demonEndRewardConfig.getDifficultId(), demonEndRewardConfig.getFeatsRank()), demonEndRewardConfig);
		}
	}
	
	public static DemonEndRewardConfig get(int difficult, int rank) {
		String key = getKey(difficult, rank);
		if (MAP.containsKey(key)) {
			return MAP.get(key);
		}
//		LOGGER.warn(String.format("不存在DemonEndRewardConfig， difficult:[%s], rank:[%s]", difficult, rank));
		return null;
	}
	
	private static String getKey(int difficult, int rank) {
		return String.valueOf(difficult).concat("-").concat(String.valueOf(rank));
	}


}
