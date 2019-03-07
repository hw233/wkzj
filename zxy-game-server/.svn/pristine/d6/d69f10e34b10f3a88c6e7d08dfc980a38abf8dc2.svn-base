package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.PowerRewardConfig;
/**
 * 最强势力排行奖励
 * @author pengzy
 *
 */
@Component
public class PowerRewardService  extends ServiceAdapter {

	private static Map<String, PowerRewardConfig> POWER_REWARD_CONFIG_MAP = new HashMap<>();
	
	@Override
	public void clear() {
		POWER_REWARD_CONFIG_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<PowerRewardConfig> list = dataConfig.listAll(this, PowerRewardConfig.class);
		for (PowerRewardConfig config : list) {
			POWER_REWARD_CONFIG_MAP.put(config.rank, config);
		}
	}
	
	public static PowerRewardConfig get(long rank){
		for(Entry<String,PowerRewardConfig> entry:POWER_REWARD_CONFIG_MAP.entrySet()){
			PowerRewardConfig powerRewardConfig = entry.getValue();
			if(powerRewardConfig.startRank <= rank && rank <= powerRewardConfig.endRank){
				return powerRewardConfig;
			}
		}
		return null;
	}
	
}
