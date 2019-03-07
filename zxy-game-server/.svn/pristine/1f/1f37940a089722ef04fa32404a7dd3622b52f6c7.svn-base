package com.jtang.gameserver.module.power.constant;

import java.util.List;

import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.service.GlobalService;

public class PowerRule {
	
	/**
	 * 最强势力排行榜上榜等级
	 */
	public static int POWER_UP_LEVEL = 15;

	/**
	 * 最强势力排行挑战的战斗地图Id
	 */
	public static int POWER_RANK_BATTLE_1V1_MAP_ID; // = 4;
	
	/**
	 * 最强势力排行的条目数
	 */
	public static int POWER_MAX_NUM_LIMIT; // = 50;
	
	/**
	 * 前三名获得的仙人魂魄奖励
	 */
	private static String POWER_REWARD_HERO_SOUL_POOL; // = "101_102_103";
	
	/**
	 * 发放奖励的时间，晚上八点，时间点的填法为1-24
	 */
	public static int POWER_AWARD_TIME; // = 20;
	
	/**
	 * 每次挑战需要消耗精力数
	 */
	public static int POWER_FIGHT_COST_ENERGY; // = 2;
	
	
	private static List<Integer> HERO_SOUL_POOL;
	
	/**
	 * 发奖最小排行
	 */
	public static int MIN_RANK = 1;
	
	/**
	 * 发奖最大排行
	 */
	public static int MAX_RANK = 20000;
	
	/**
	 * 前三名奖励的魂魄id
	 */
	private static int REWARD_HERO_SOUL_ID;

	static {
		POWER_UP_LEVEL = GlobalService.getInt("POWER_UP_LEVEL");
		POWER_RANK_BATTLE_1V1_MAP_ID = GlobalService.getInt("POWER_RANK_BATTLE_1V1_MAP_ID");
		POWER_MAX_NUM_LIMIT = GlobalService.getInt("POWER_MAX_NUM_LIMIT");
		POWER_REWARD_HERO_SOUL_POOL = GlobalService.get("POWER_REWARD_HERO_SOUL_POOL");
		POWER_AWARD_TIME = GlobalService.getInt("POWER_AWARD_TIME");
		POWER_FIGHT_COST_ENERGY = GlobalService.getInt("POWER_FIGHT_COST_ENERGY");
		MIN_RANK = GlobalService.getInt("MIN_RANK");
		MAX_RANK = GlobalService.getInt("MAX_RANK");
		
		HERO_SOUL_POOL = StringUtils.delimiterString2IntList(POWER_REWARD_HERO_SOUL_POOL, Splitable.ATTRIBUTE_SPLIT);
	}
	
	/**
	 * 获取今天该奖励的魂魄id
	 * @return
	 */
	public static int getTodayRewardSouldId() {
		return REWARD_HERO_SOUL_ID;
	}
	
	/**
	 * 重新生成新的魂魄id
	 */
	public static void randRewardSoulId() {
		int index = RandomUtils.nextIntIndex(HERO_SOUL_POOL.size());
		REWARD_HERO_SOUL_ID = HERO_SOUL_POOL.get(index);
	}
	
}
