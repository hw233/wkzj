package com.jtang.gameserver.module.ally.constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.service.GlobalService;
import com.jtang.gameserver.module.battle.constant.WinLevel;


public class AllyRule {

	/**
	 * 可加盟的人数上限
	 */
	public static int ALLY_ALLIES_LIMIT = 10;
	
	/**
	 * 加好友被加好友关卡限制
	 */
	public static int ADD_ALLY_STORY_LOCK = 100307;
	/**
	 * 重复添加盟友的时间限制
	 */
	public static int ALLY_REPEAT_BEYOND_TIME = 86400;
	
	/**
	 * 切磋消耗精力值
	 */
	public static int ALLY_FIGHT_NEED_ENERGY = 3;
	
	/**
	 * 与单个盟友每天可切磋次数上限
	 */
	public static int ALLY_FIGHT_NUM_LIMIT = 2;
	/**
	 * 战场ID，所有玩家都一样的战场配置
	 */
	public static int ALLY_FIGHT_MAP_ID = 1;
	
	/**
	 * 每日可切磋的总次数
	 */
	public static int ALLY_FIGHT_DAY_NUM_LIMIT = 4;
	/**
	 * 每日可切磋次数重置时间
	 */
	public static int ALLY_FIGHT_COUNT_RESET_TIME = 3;
	/**
	 * 切磋奖励的最低等级差
	 */
	public static int ALLY_FIGHT_REWARD_MIN_LEVEL_DIFFER = 5;
	
	/**
	 * 大败1_气势值|失败2_气势值|惜败3_气势值|小胜4_气势值|中胜5_气势值|大胜6_气势值
	 * reward morale="1_2|2_3|3_5|4_7|5_8|6_10"
	 */
	
	public static String ALLY_FIGHT_REWARD_EXPR = "1_2|2_3|3_5|4_7|5_8|6_10";
	
	/**
	 * 刷新加好友列表人数
	 */
	public static int FLUSH_ALLYS_LIST = 3;
	
	/**
	 * 刷新加好友列表等级区间
	 */
	public static int FLUSH_ALLYS_LEVEL_LIMIT = 5;
	
	public static Map<WinLevel, Integer> ALLY_FIGHT_REWARD = new HashMap<WinLevel, Integer>();
	
	static {
		ALLY_ALLIES_LIMIT = GlobalService.getInt("ALLY_ALLIES_LIMIT");
		ALLY_REPEAT_BEYOND_TIME = GlobalService.getInt("ALLY_REPEAT_BEYOND_TIME");
		ALLY_FIGHT_NEED_ENERGY = GlobalService.getInt("ALLY_FIGHT_NEED_ENERGY");
		ALLY_FIGHT_NUM_LIMIT = GlobalService.getInt("ALLY_FIGHT_NUM_LIMIT");
		ALLY_FIGHT_MAP_ID = GlobalService.getInt("ALLY_FIGHT_MAP_ID");
		ALLY_FIGHT_DAY_NUM_LIMIT = GlobalService.getInt("ALLY_FIGHT_DAY_NUM_LIMIT");
		ALLY_FIGHT_COUNT_RESET_TIME = GlobalService.getInt("ALLY_FIGHT_COUNT_RESET_TIME");
		ALLY_FIGHT_REWARD_MIN_LEVEL_DIFFER = GlobalService.getInt("ALLY_FIGHT_REWARD_MIN_LEVEL_DIFFER");
		ALLY_FIGHT_REWARD_EXPR = GlobalService.get("ALLY_FIGHT_REWARD_EXPR");
		FLUSH_ALLYS_LIST = GlobalService.getInt("FLUSH_ALLYS_LIST");
		FLUSH_ALLYS_LEVEL_LIMIT = GlobalService.getInt("FLUSH_ALLYS_LEVEL_LIMIT");
		ADD_ALLY_STORY_LOCK = GlobalService.getInt("ADD_ALLY_STORY_LOCK");
		initAllyFightReward();		
	}

	private static void initAllyFightReward() {
		List<String> rewardList = StringUtils.delimiterString2List(ALLY_FIGHT_REWARD_EXPR, Splitable.ELEMENT_SPLIT);
		for (String reward : rewardList) {			
			List<String> moraleReward = StringUtils.delimiterString2List(reward, Splitable.ATTRIBUTE_SPLIT);			
			//胜利的类型
			int level = Integer.valueOf(moraleReward.get(0));
			//奖励气势值
			int value = Integer.valueOf(moraleReward.get(1));
			WinLevel winLv = WinLevel.getByCode(level);
			ALLY_FIGHT_REWARD.put(winLv, value);
		}
	}
}
