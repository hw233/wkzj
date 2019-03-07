package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.PlantConfig;
import com.jtang.gameserver.dataconfig.model.PlantGlobalConfig;
import com.jtang.gameserver.dataconfig.model.RewardConfig;

@Component
public class PlantService extends ServiceAdapter {

	private static Map<Integer, PlantConfig> PLANT_CONFIG_MAP = new HashMap<>();

	private static List<PlantGlobalConfig> PLANT_GLOBAL_LIST = new ArrayList<>();

	@Override
	public void clear() {
		PLANT_CONFIG_MAP.clear();
		PLANT_GLOBAL_LIST.clear();
	}

	@Override
	public void initialize() {

		List<PlantConfig> list = dataConfig.listAll(this, PlantConfig.class);
		for (PlantConfig config : list) {
			PLANT_CONFIG_MAP.put(config.plantId, config);
		}

		List<PlantGlobalConfig> globalList = dataConfig.listAll(this,
				PlantGlobalConfig.class);
		for (PlantGlobalConfig config : globalList) {
			PLANT_GLOBAL_LIST.add(config);
		}
	}

	/**
	 * 获取种植物品的配置
	 * 
	 * @param plant
	 * @return
	 */
	public static PlantConfig getPlantConfig(int plant) {
		return PLANT_CONFIG_MAP.get(plant);
	}

	/**
	 * 获取固定奖励 随机多个出一个
	 */
	public static RewardObject getPlantMastReward(int plant, int level) {
		PlantConfig plantConfig = getPlantConfig(plant);
		List<RewardConfig> list = plantConfig.rewardList;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < list.size(); i++) {
			map.put(i, list.get(i).proportion);
		}
		Integer index = RandomUtils.randomHit(1000, map);
		if(index == null){
			return null;
		}
		RewardConfig reward = list.get(index);
		int num = FormulaHelper.executeCeilInt(reward.num, level);
		RewardObject rewardObject = new RewardObject(
				RewardType.getType(reward.type), reward.rewardId, num);
		return rewardObject;
	}

	/**
	 * 获取保底奖励 随机多个出一个
	 * 
	 * @return
	 */
	public static RewardObject getPlantReward(int level) {
		PlantGlobalConfig globalConfig = getPlantGlobalConfig();
		List<RewardConfig> list = globalConfig.rewardList;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < list.size(); i++) {
			map.put(i, list.get(i).proportion);
		}
		Integer index = RandomUtils.randomHit(1000, map);
		if(index == null){
			return null;
		}
		RewardConfig reward = list.get(index);
		int num = FormulaHelper.executeCeilInt(reward.num, level);
		RewardObject rewardObject = new RewardObject(
				RewardType.getType(reward.type), reward.rewardId, num);
		return rewardObject;
	}

	public static PlantGlobalConfig getPlantGlobalConfig() {
		return PLANT_GLOBAL_LIST.get(0);
	}

	/**
	 * 获取加速没X分钟消耗x点券
	 * 
	 * @return
	 */
	public static Entry<Integer, Integer> getPlantQuickenCostTicket() {
		PlantGlobalConfig config = PLANT_GLOBAL_LIST.get(0);
		for (Entry<Integer, Integer> entry : config.costTicketMap.entrySet()) {
			return entry;
		}
		return null;
	}

	/**
	 * 额外奖励,随机多个有几率不出
	 * 
	 * @return
	 */
	public static RewardObject getExtReward(int level) {
		PlantGlobalConfig config = getPlantGlobalConfig();
		List<RewardConfig> list = config.extRewardList;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < list.size(); i++) {
			map.put(i, list.get(i).proportion);
		}
		Integer index = RandomUtils.randomHit(1000, map);
		if (index == null) {
			return null;
		} else {
			RewardConfig reward = list.get(index);
			int num = FormulaHelper.executeCeilInt(reward.num, level);
			RewardObject rewardObject = new RewardObject(
					RewardType.getType(reward.type), reward.rewardId, num);
			return rewardObject;
		}
	}

	/**
	 * 获取所有的固定奖励
	 * 
	 * @param level
	 * @return
	 */
	public static List<RewardObject> getAllReward(int level) {
		List<RewardObject> list = new ArrayList<>();
		for (Integer key : PLANT_CONFIG_MAP.keySet()) {
			PlantConfig config = PLANT_CONFIG_MAP.get(key);
			for(RewardConfig rewardConfig : config.rewardList){
				RewardType rewardType = RewardType.getType(rewardConfig.type);
				int num = FormulaHelper.executeCeilInt(rewardConfig.num,level);
				RewardObject rewardObject = new RewardObject(rewardType,rewardConfig.rewardId,num);
				list.add(rewardObject);
			}
		}
		return list;
	}

	/**
	 * 获取所有的额外奖励
	 */
	public static List<RewardObject> getAllExtReward(int level) {
		List<RewardObject> list = new ArrayList<>();
		PlantGlobalConfig config = getPlantGlobalConfig();
		for (RewardConfig reward : config.extRewardList) {
			RewardType type = RewardType.getType(reward.type);
			int num = FormulaHelper.executeCeilInt(reward.num, level);
			RewardObject rewardObject = new RewardObject(type,reward.rewardId,num);
			list.add(rewardObject);
		}
		return list;
	}

}
