package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.BasinConfig;
import com.jtang.gameserver.dataconfig.model.BasinRewardConfig;

@Component
public class BasinService extends ServiceAdapter {
	
	private static Map<Integer,BasinRewardConfig> REWARD_MAP = new HashMap<>();
	
	private static BasinConfig GLOBAL_CONFIG = new BasinConfig();

	@Override
	public void clear() {
		REWARD_MAP.clear();
		GLOBAL_CONFIG = new BasinConfig();
	}

	@Override
	public void initialize() {
		List<BasinRewardConfig> rewardList = dataConfig.listAll(this, BasinRewardConfig.class);
		for(BasinRewardConfig config : rewardList){
			REWARD_MAP.put(config.recharge, config);
		}
		
		List<BasinConfig> globalList = dataConfig.listAll(this, BasinConfig.class);
		for(BasinConfig config : globalList){
			GLOBAL_CONFIG = config;
		}
	}
	
	public static BasinConfig getGlobalConfig(){
		return GLOBAL_CONFIG;
	}
	
	public static BasinRewardConfig getRewardConfig(int recharge){
		return REWARD_MAP.get(recharge);
	}
	 
	public static List<BasinRewardConfig> getAllRewardConfig(){
		return new ArrayList<>(REWARD_MAP.values());
	}

}
