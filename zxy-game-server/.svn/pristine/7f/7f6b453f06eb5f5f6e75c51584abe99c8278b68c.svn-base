package com.jtang.gameserver.module.story.constant;

import java.util.List;

import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.service.GlobalService;

/**
 * 故事模块常量定义
 * @author vinceruan
 *
 */
public class StoryRule {
	/**
	 * 故事的声望奖励规则
	 * <pre>
	 * 玩家掌教等级-故事标准等级<=5时，获得100%声望
	 * 10>玩家掌教等级-故事标准等级>5时，获得50%声望
	 * 玩家掌教等级-故事标准等级>=10时，获得0%声望
	 * </pre>
	 */
	public static String STORY_REWARD_RULE;  // = "-1000_5_100|5_10_50|10_1000_0";
	
	public static List<String[]> STORY_REWARD_RULE_LIST;
	
	public static byte STORY_FIGHT_STAR = 3;
	/**
	 * 扫荡开启章节
	 */
	public static int MUTI_FIGHT_STORY_ID = 0;
	
	/**
	 * 合作关卡盟友与自身等级间隔
	 */
	public static int ALLY_BATTLE_LEVEL_LIMIT = 5;
	
	/**
	 * 合作关卡邀请盟友有奖励次数限制
	 */
	public static int ALLY_FIGHT_REWARD_NUM = 5;
	
	static {
		STORY_REWARD_RULE = GlobalService.get("STORY_REWARD_RULE");
		STORY_REWARD_RULE_LIST = StringUtils.delimiterString2Array(STORY_REWARD_RULE);
		STORY_FIGHT_STAR = GlobalService.getByte("STORY_FIGHT_STAR");
		MUTI_FIGHT_STORY_ID = GlobalService.getInt("MUTI_FIGHT_STORY_ID");
		ALLY_BATTLE_LEVEL_LIMIT = GlobalService.getInt("ALLY_BATTLE_LEVEL_LIMIT");
		ALLY_FIGHT_REWARD_NUM = GlobalService.getInt("ALLY_FIGHT_REWARD_NUM");
	}
	
	public static int getRate(int diff){
		for (String[] conf : STORY_REWARD_RULE_LIST) {
			if (diff > Integer.valueOf(conf[0]) && diff < Integer.valueOf(conf[1])) {
				return Integer.valueOf(conf[2]);
			}
		}
		return 100;
	}
}
