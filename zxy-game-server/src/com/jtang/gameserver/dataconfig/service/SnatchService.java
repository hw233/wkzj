package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.SnatchAchieveConfig;
import com.jtang.gameserver.dataconfig.model.SnatchConfig;
import com.jtang.gameserver.dataconfig.model.SnatchEnemyLevelConfig;
import com.jtang.gameserver.dataconfig.model.SnatchRewardConfig;
import com.jtang.gameserver.dataconfig.model.SnatchTimeConfig;
import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;

/**
 * 抢夺配置
 * @author liujian
 *
 */
@Component
public class SnatchService extends ServiceAdapter {

	/**
	 *  抢夺配置,格式是Map<type_star, SnatchConfig>
	 */
	private static SnatchConfig SNATCH_CFG = new SnatchConfig(); 
	
	/**
	 * 抢夺对手配置
	 */
	private static Map<Integer, SnatchEnemyLevelConfig> SNATCH_ENEMY_CONFIG = new HashMap<>();
	
	/**
	 * 抢夺奖励配置  key:当前等级  value:抢夺奖励配置
	 */
	private static Map<Integer, List<SnatchRewardConfig>> SNATCH_REWARD_CONFIG = new HashMap<>();
	
	/**
	 * 抢夺时间配置
	 */
	private static List<SnatchTimeConfig> SNATCH_TIME_CONFIG = new ArrayList<>();
	
	/**
	 * 抢夺成就配置
	 */
	private static Map<Integer,SnatchAchieveConfig> SNATCH_ACHIEVE_MAP = new HashMap<>();
	
	@Override
	public void clear() {
		SNATCH_ENEMY_CONFIG.clear();
		SNATCH_REWARD_CONFIG.clear();
		SNATCH_TIME_CONFIG.clear();
		SNATCH_ACHIEVE_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<SnatchConfig> snatchList = dataConfig.listAll(this, SnatchConfig.class);
		for (SnatchConfig snatch : snatchList) {
			SNATCH_CFG = snatch;
		}
		
		List<SnatchEnemyLevelConfig> enemyLevelList = dataConfig.listAll(this, SnatchEnemyLevelConfig.class);
		for (SnatchEnemyLevelConfig config : enemyLevelList) {
			SNATCH_ENEMY_CONFIG.put(config.getActorLevel(), config);
		}
		
		List<SnatchRewardConfig> rewardList = dataConfig.listAll(this, SnatchRewardConfig.class);
		for (SnatchRewardConfig snatchRewardConfig : rewardList) {
			List<SnatchRewardConfig> rewardLevelGroupMaps = SNATCH_REWARD_CONFIG.get(snatchRewardConfig.getSnatchLevel());
			if (rewardLevelGroupMaps == null) {
				rewardLevelGroupMaps = new ArrayList<>();
				SNATCH_REWARD_CONFIG.put(snatchRewardConfig.getSnatchLevel(), rewardLevelGroupMaps);
			}
			rewardLevelGroupMaps.add(snatchRewardConfig);
		}
		
		List<SnatchTimeConfig> timeConfig = dataConfig.listAll(this, SnatchTimeConfig.class);
		SNATCH_TIME_CONFIG.addAll(timeConfig);
		
		List<SnatchAchieveConfig> achieveConfig = dataConfig.listAll(this, SnatchAchieveConfig.class);
		for(SnatchAchieveConfig config:achieveConfig){
			SNATCH_ACHIEVE_MAP.put(config.achieveId, config);
		}
	}
	
	/**
	 * 获取抢夺配置
	 * @param exType  类型 {@code SnatchType}
	 * @param star  星级
	 * @return
	 */
	public static SnatchConfig get() {
		return SNATCH_CFG;
	}
	
	/**
	 * 获取机器人下限等级
	 * @param actorLevel
	 * @return
	 */
	public static int robotLevelLowerLimit(int actorLevel) {
		SnatchEnemyLevelConfig config = SNATCH_ENEMY_CONFIG.get(actorLevel);
		if(config == null) {
			return 1;
		}
		return config.getRobotLowerLimit();
	}

	/**
	 * 获取机器人上限等级
	 * @param actorLevel
	 * @return
	 */
	public static int robotLevelUpperLimit(int actorLevel) {
		SnatchEnemyLevelConfig config = SNATCH_ENEMY_CONFIG.get(actorLevel);
		if(config == null) {
			return 1;
		}
		return config.getRobotUpperLimit();
	}
	
	/**
	 * 获取真实角色下限等级
	 * @param actorLevel
	 * @return
	 */
	public static int actorLevelLowerLimit(int actorLevel) {
		SnatchEnemyLevelConfig config = SNATCH_ENEMY_CONFIG.get(actorLevel);
		if (config == null) {
			return 1;
		}
		return config.getActorLowerLimit();
	}
	
	/**
	 * 获取真实角色上限等级
	 * @param actorLevel
	 * @return
	 */
	public static int actorLevelUpperLimit(int actorLevel) {
		SnatchEnemyLevelConfig config = SNATCH_ENEMY_CONFIG.get(actorLevel);
		if (config == null) {
			return 1;
		}
		return config.getActorUpperLimit();
	}
	
	/**
	 * 获取被抢夺金币的上限
	 * @param actorLevel
	 * @return
	 */
	public static int snatchGoldLimit(int actorLevel) {
		SnatchEnemyLevelConfig config = SNATCH_ENEMY_CONFIG.get(actorLevel);
		if (config == null) {
			return 1;
		}
		return config.getSnatchGoldLimit();
	}
	
	/**
	 * 判断 当前抢夺的目标角色是否在 抢夺对手配置等级范围内
	 * @param actorLevel		抢夺者的等级
	 * @param enemyType			目标被抢者的敌人类型
	 * @param targetActorLevel	目标被抢者的角色等级
	 * @return
	 */
	public static boolean snatchTargetInLevelLimitScope(int actorLevel, SnatchEnemyType enemyType, int targetActorLevel) {
		int lowerLevel = 0;
		int upperLevel = 0;
		if (enemyType == SnatchEnemyType.ACTOR) {
			lowerLevel = actorLevelLowerLimit(actorLevel);
			upperLevel = actorLevelUpperLimit(actorLevel);
		} else {
			lowerLevel = robotLevelLowerLimit(actorLevel);
			upperLevel = robotLevelUpperLimit(actorLevel);
		}

		if (targetActorLevel >= lowerLevel && targetActorLevel <= upperLevel) {
			return true;
		}
		return false;
	}
	
	/**
	 * 抢夺后随机奖励
	 * @param actorLevel		角色等级
	 * @param targetActorLevel	被抢夺者等级
	 * @param snatchTotalNum	当前玩家抢夺胜利总次数
	 * @return
	 */
	public static RewardObject randomSnatchReward(int actorLevel, int targetActorLevel, int snatchTotalNum) {		
		List<SnatchRewardConfig> rewardConfigList = SNATCH_REWARD_CONFIG.get(actorLevel);
		if (rewardConfigList == null) {
			return null;
		}
		
		SnatchRewardConfig rewardConfig = null;
		for (SnatchRewardConfig config : rewardConfigList) {
			if (targetActorLevel >= config.getEnemyFromLevel() && targetActorLevel <= config.getEnemyToLevel()) {
				rewardConfig = config;
				break;
			}
		}
		
		if (rewardConfig == null) {
			return null;
		}
		
		if (snatchTotalNum >= rewardConfig.getSnatchTotalNum()) {
			return null;
		}
		
		if (rewardConfig.getRewardObjectList().size() < 1) {
			return null;
		}
		
		if (RandomUtils.is1000Hit(rewardConfig.getRate()) == false) {
			return null;
		}

		int index = RandomUtils.nextIntIndex(rewardConfig.getRewardObjectList().size());
		return rewardConfig.getRewardObjectList().get(index);
	}
	
	/**
	 * 是否开启了抢夺获得额外奖励
	 */
	public static boolean isOpen(){
		int now = TimeUtils.getNow();
		SnatchTimeConfig config = SNATCH_TIME_CONFIG.get(0);
		return config.start <= now && now < config.end;
	}
	
	/**
	 * 获取第一个成就
	 */
	public static List<SnatchAchieveConfig> getFirstAchieve(){
		List<SnatchAchieveConfig> list = new ArrayList<>();
		for(SnatchAchieveConfig config:SNATCH_ACHIEVE_MAP.values()){
			if(config.priorId == 0){
				list.add(config);
			}
		}
		return list;
	}
	
	/**
	 * 获取下一个成就
	 */
	public static SnatchAchieveConfig getNextAchieve(int achieveId){
		for(SnatchAchieveConfig config:SNATCH_ACHIEVE_MAP.values()){
			if(config.priorId == achieveId){
				return config;
			}
		}
		return null;
	}
	
	/**
	 * 获取成就
	 */
	public static SnatchAchieveConfig getAchieve(int achieveId){
		return SNATCH_ACHIEVE_MAP.get(achieveId);
	}

}
