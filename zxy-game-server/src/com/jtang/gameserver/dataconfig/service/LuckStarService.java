package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.LuckStarConfig;
import com.jtang.gameserver.dataconfig.model.LuckStarRewardConfig;

@Component
public class LuckStarService extends ServiceAdapter {

	public static Map<Integer, LuckStarRewardConfig> LUCKSTAR_REWARD = new HashMap<>();

	public static List<LuckStarConfig> LUCKSTAR = new ArrayList<>();

	@Override
	public void clear() {
		LUCKSTAR_REWARD.clear();
	}

	@Override
	public void initialize() {
		List<LuckStarRewardConfig> list = dataConfig.listAll(this, LuckStarRewardConfig.class);
		for (LuckStarRewardConfig config : list) {
			LUCKSTAR_REWARD.put(config.id, config);
		}

		LUCKSTAR.addAll(dataConfig.listAll(this, LuckStarConfig.class));
	}

	public static LuckStarRewardConfig random() {
		Map<Integer,Integer> map = new HashMap<>();
		for (Integer key : LUCKSTAR_REWARD.keySet()) {
			LuckStarRewardConfig config = LUCKSTAR_REWARD.get(key);
			map.put(key, config.proportion);
		}
		Integer id = RandomUtils.randomHit(1000, map);
		if(id == null){
			return null;
		}
		return LUCKSTAR_REWARD.get(id);
	}
	
	public static LuckStarConfig getLuckStarConfig(){
		return LUCKSTAR.get(0);
	}
	
	public static LuckStarRewardConfig getReward(int id){
		return LUCKSTAR_REWARD.get(id);
	}
	
	public static LuckStarRewardConfig getMastReward(){
		int mustId = LuckStarService.getLuckStarConfig().mustRewardId;
		LuckStarRewardConfig rewardConfig = LuckStarService.getReward(mustId);
		return rewardConfig;
	}

}
