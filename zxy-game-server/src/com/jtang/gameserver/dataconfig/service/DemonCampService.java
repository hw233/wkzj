package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.DemonCampConfig;

/**
 * 集众降魔配置服务
 * @author ludd
 *
 */
@Component
public class DemonCampService extends ServiceAdapter {
	
	/**
	 * key:difficult
	 */
	private static Map<Integer, List<DemonCampConfig>> MAP = new HashMap<Integer, List<DemonCampConfig>>(); 
	
	@Override
	public void clear() {
		MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<DemonCampConfig> list = dataConfig.listAll(this, DemonCampConfig.class);
		for (DemonCampConfig demonCampConfig : list) {
			List<DemonCampConfig> configs = null;
			if (MAP.containsKey(demonCampConfig.getDifficult())) {
				configs = MAP.get(demonCampConfig.getDifficult());
			} else {
				configs = new ArrayList<>();
				MAP.put(demonCampConfig.getDifficult(), configs);
			}
			configs.add(demonCampConfig);
		}
	}
	
	/**
	 * 随机一个配置
	 * @param difficult
	 * @return
	 */
	public static DemonCampConfig getRandomConfig(int difficult) {
		if (MAP.containsKey(difficult) == false) {
			return null;
		}
		List<DemonCampConfig> configs = MAP.get(difficult);
		int index = RandomUtils.nextIntIndex(configs.size());
		return configs.get(index);
		
	}
	
	

}
