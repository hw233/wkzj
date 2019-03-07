package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.RandomRewardConfig;
import com.jtang.gameserver.dataconfig.model.RandomRewardPoolConfig;
import com.jtang.gameserver.module.extapp.randomreward.model.RewardVO;

@Component
public class RandomRewardService extends ServiceAdapter {
	
	private static Map<Integer,RandomRewardConfig> REWARD_MAP = new HashMap<>();
	
	private static Map<Integer,List<RandomRewardPoolConfig>> POOL_MAP = new HashMap<>();

	@Override
	public void clear() {
		REWARD_MAP.clear();
		POOL_MAP.clear();
	}

	@Override
	public void initialize() {
		List<RandomRewardConfig> rewardList = dataConfig.listAll(this, RandomRewardConfig.class);
		for(RandomRewardConfig config : rewardList){
			REWARD_MAP.put(config.id, config);
		}
		
		List<RandomRewardPoolConfig> poolList=  dataConfig.listAll(this, RandomRewardPoolConfig.class);
		for(RandomRewardPoolConfig config : poolList){
			if(POOL_MAP.containsKey(config.rewardType)){
				POOL_MAP.get(config.rewardType).add(config);
			}else{
				List<RandomRewardPoolConfig> list = new ArrayList<>();
				list.add(config);
				POOL_MAP.put(config.rewardType, list);
			}
		}
	}
	
	public static RandomRewardConfig getRewardConfig(int id){
		return REWARD_MAP.get(id);
	}
	
	public static List<RandomRewardPoolConfig> getPoolList(int rewardType){
		return POOL_MAP.get(rewardType);
	}
	
	public static RandomRewardPoolConfig getReward(int level,int id){
		RandomRewardConfig rewardConfig = getRewardConfig(id);
		Integer rewardType = RandomUtils.randomHit(1000, rewardConfig.map);
		if(rewardType == null){
			return null;
		}
		List<RandomRewardPoolConfig> poolList = getPoolList(rewardType);
		Map<Integer,Integer> randomMap = new HashMap<>();
		for (int i = 0; i < poolList.size(); i++) {
			RandomRewardPoolConfig config = poolList.get(i);
			randomMap.put(i, config.rate);
		}
		rewardType = RandomUtils.randomHit(1000, randomMap);
		if(rewardType == null){
			return null;
		}
		RandomRewardPoolConfig config = poolList.get(rewardType);
		return config;
	}
	
	public static Map<Integer,RewardVO> init(){
		Map<Integer,RewardVO> map = new HashMap<>();
		for(RandomRewardConfig randomRewardConfig : REWARD_MAP.values()){
			RewardVO rewardVO = new RewardVO();
			rewardVO.id = randomRewardConfig.id;
			rewardVO.flushTime = TimeUtils.getNow() + randomRewardConfig.getFlushTime();
			map.put(randomRewardConfig.id, rewardVO);
		}
		return map;
	}

}
