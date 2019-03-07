package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.SignRewardConfig;

@Component
public class SignService extends ServiceAdapter {
	
	/**
	 * key 月 value key 日 value 配置
	 */
	private static Map<Integer, Map<Integer,SignRewardConfig>> REWARD_CONFIG_MAP = new HashMap<>();
	
	/**
	 * key 月 value key 日 value 配置
	 */
	private static Map<Integer, Map<Integer,SignRewardConfig>> LUXURY_CONFIG_MAP = new HashMap<>();
	
	@Override
	public void clear() {
		REWARD_CONFIG_MAP.clear();
		LUXURY_CONFIG_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<SignRewardConfig> rewardConfig = this.dataConfig.listAll(this, SignRewardConfig.class);
		for (SignRewardConfig config : rewardConfig) {
			if(config.isLuxury()){
				if(LUXURY_CONFIG_MAP.containsKey(config.month)){
					LUXURY_CONFIG_MAP.get(config.month).put(config.day, config);
				}else{
					Map<Integer,SignRewardConfig> map = new HashMap<>();
					map.put(config.day, config);
					LUXURY_CONFIG_MAP.put(config.month, map);
				}
			}else{
				if(REWARD_CONFIG_MAP.containsKey(config.month)){
					REWARD_CONFIG_MAP.get(config.month).put(config.day, config);
				}else{
					Map<Integer,SignRewardConfig> map = new HashMap<>();
					map.put(config.day, config);
					REWARD_CONFIG_MAP.put(config.month, map);
				}
			}
		}
	}

	public static SignRewardConfig getSignReward(int day, int month) {
		return REWARD_CONFIG_MAP.get(month).get(day);
	}
	
	public static SignRewardConfig getLuxuryReward(int day,int month){
		return LUXURY_CONFIG_MAP.get(month).get(day);
	}
	
}
