package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.dataconfig.model.SprintGiftConfig;
import com.jtang.gameserver.dataconfig.model.SprintGiftVIPConvertConfig;
import com.jtang.gameserver.module.sprintgift.type.SprintGiftStatusType;

/**
 * 冲级礼包使用物品服务类
 * @author ligang
 *
 */
@Component
public class SprintGiftService extends ServiceAdapter {

	/**
	 * 所有SprintGiftConfig 集合
	 * 格式 Map<领取等级, SprintGiftConfig>
	 */
	private static Map<Integer, SprintGiftConfig> SPRINT_GIFT_CONFIG_MAP = new HashMap<>();
	
	/**
	 * 所有SprintGiftVIPConvertConfig 集合
	 * 格式 Map<领取等级, SprintGiftVIPConvertConfig>
	 */
	private static Map<Integer, SprintGiftVIPConvertConfig> SPRINT_GIFT_VIP_CONFIG_MAP = new HashMap<>();
	
	@Override
	public void clear() {
		SPRINT_GIFT_CONFIG_MAP.clear();
		SPRINT_GIFT_VIP_CONFIG_MAP.clear();
	}

	@Override
	public void initialize() {
		List<SprintGiftConfig> allGiftConfigs = dataConfig.listAll(this,SprintGiftConfig.class);
		for (SprintGiftConfig config : allGiftConfigs) {
			SPRINT_GIFT_CONFIG_MAP.put(config.getLevel(), config);
		}
		
		List<SprintGiftVIPConvertConfig> vipConvertGiftConfigs = dataConfig.listAll(this,SprintGiftVIPConvertConfig.class);
		for (SprintGiftVIPConvertConfig config : vipConvertGiftConfigs) {
			SPRINT_GIFT_VIP_CONFIG_MAP.put(config.getVipLevel(), config);
		}
	}
	
	/**
	 * 根据领取等级和自己的vip等级获取奖励物品。
	 * 	如果领取等级对应配置奖励物品有vip权限礼包，则将赠送的vip权限等级和玩家的vip等级比较，玩家的vip等级大于赠送的vip等级，
	 * 需要将赠送的vip权限礼包转换为物品发给玩家
	 * @param level 领取等级
	 * @param selfVIPLevel 玩家vip等级
	 * @return List<RewardObject> 奖励物品集合，（如果有配置vip权限礼包并且赠送等级小于等于玩家vip等级，则包含赠送vip权限礼包转换的物品）
	 */
	public static List<RewardObject> getReward(int level, int selfVIPLevel, int vipGiftLevel) {
		if (SPRINT_GIFT_CONFIG_MAP.containsKey(level) == false) {
			return new ArrayList<RewardObject>();
		}
		SprintGiftConfig config = SPRINT_GIFT_CONFIG_MAP.get(level);
		List<RewardObject> rewardObjects = new ArrayList<RewardObject>(config.getRewardsList());
		if (vipGiftLevel > 0) {
			if (vipGiftLevel <= selfVIPLevel) {
				SprintGiftVIPConvertConfig vipConfig = SPRINT_GIFT_VIP_CONFIG_MAP.get(vipGiftLevel);
				if (vipConfig != null) {
					rewardObjects.addAll(vipConfig.getConvertRewardsList());
				}
			}
		}
		return rewardObjects;
	}
	
	/**
	 * 根据领取等级获得对应配置中的vip权限礼包，如果对应配置中没有赠送vip礼包， 返回0；
	 * @param level 领取等级
	 * @return vip权限礼包的等级
	 */
	public static int getSprintGiftVIPLevelByLevel(int level) {
		if (SPRINT_GIFT_CONFIG_MAP.containsKey(level) == false) {
			return 0;
		}
		SprintGiftConfig config = SPRINT_GIFT_CONFIG_MAP.get(level);
		int vipGiftLevel = config.getVipLevel();
		return vipGiftLevel;
	}
	
	/**
	 * 默认是所有等级礼包未领取，
	 * @return Map<领取等级, 对应等级礼包领取状态> defaultMap
	 */
	public static Map<Integer, Integer> getDefaultStatusMap() {
		Map<Integer, Integer> defaultMap = new HashMap<Integer, Integer>();
		for (Map.Entry<Integer, SprintGiftConfig> item : SPRINT_GIFT_CONFIG_MAP.entrySet()) {
			defaultMap.put(item.getKey(), SprintGiftStatusType.DO_NOT_RECEIVE.getType());
		}
		return defaultMap;
	}
}