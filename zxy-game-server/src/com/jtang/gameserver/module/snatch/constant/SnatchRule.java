package com.jtang.gameserver.module.snatch.constant;

import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.service.GlobalService;

/**
 * 抢夺常量定义
 * @author liujian
 *
 */
public class SnatchRule {
		
	/**
	 * 机器人维护金币时间:秒
	 */
	public static int SNATCH_ROBOT_MAINTAIN_TIME;
	
	/**
	 * 系统启动时根据该参数查询DB里面最近N天登陆的玩家, 然后构建被抢夺玩家缓存列表
	 * 单位:秒(详见下面赋值处理)
	 */
	public static int SNATCH_LOGIN_DAY_4_CACHE;
	
	/**
	 * 系统启动时加载到抢夺缓存的玩家id的数量
	 */
	public static int SNATCH_ACTOR_NUM_4_CACHE;
		
	/**
	 * 机器人金币的下限(百分比,分母为100),少于这个阀值机器人的金币就会自动恢复
	 */
	public static int SNATCH_ROBOT_GOLD_LOWER_LIMIT;
	
	/**
	 * 机器人金币每次恢复量(百分比,分母为100)
	 */
	public static int SNATCH_ROBOT_GOLD_EACH_RECOER;
	
	/**
	 * 特定等级范围内的掌教抢夺金币时只会遇到机器人
	 */
	private static int SNATCH_ONLY_ROBOT_GOLD_MIN_LEVEL;
	
	/**
	 * 特定等级范围内的掌教抢夺金币时只会遇到机器人
	 */
	private static int SNATCH_ONLY_ROBOT_GOLD_MAX_LEVEL;
	
	/**
	 * 抢夺排行榜上面保留最近抢/被抢敌人的数量
	 */
	public static int SNATCH_RESERVE_ENEMY_COUNT;
	
	/**
	 * 抢夺排行榜上面保留最近抢/被抢敌人的最长时间(单位天)
	 */
	public static int SNATCH_RESERVE_ENEMY_DAY;

	/**
	 * 切换对手列表显示敌人数量
	 */
	public static int SNATCH_ENEMY_DISPLAY_NUM;
	
	/**
	 * 切换对手时至少出现的机器人个数
	 */
	private static int SNATCH_ENEMY_LEAST_ROBOT_NUM;
	
	/**
	 * 切换对手时出现真实玩家的百分比(1-100)
	 */
	public static int SNTACH_ENEMY_REAL_ACTOR_PERCENT;
	
	/**
	 * 角色超过多少等级则可以开战跳过战斗
	 */
	public static int SNATCH_EXCEED_LEVEL_PASS_BATTLE  = 30;
	
	static {
		SNATCH_ROBOT_MAINTAIN_TIME = GlobalService.getInt("SNATCH_ROBOT_MAINTAIN_TIME");
		SNATCH_LOGIN_DAY_4_CACHE = GlobalService.getInt("SNATCH_LOGIN_DAY_4_CACHE") * 24 * 3600;
		SNATCH_ACTOR_NUM_4_CACHE = GlobalService.getInt("SNATCH_ACTOR_NUM_4_CACHE");
		SNATCH_ROBOT_GOLD_LOWER_LIMIT = GlobalService.getInt("SNATCH_ROBOT_GOLD_LOWER_LIMIT");
		SNATCH_ROBOT_GOLD_EACH_RECOER = GlobalService.getInt("SNATCH_ROBOT_GOLD_EACH_RECOER");
		SNATCH_ONLY_ROBOT_GOLD_MIN_LEVEL = GlobalService.getInt("SNATCH_ONLY_ROBOT_GOLD_MIN_LEVEL");
		SNATCH_ONLY_ROBOT_GOLD_MAX_LEVEL = GlobalService.getInt("SNATCH_ONLY_ROBOT_GOLD_MAX_LEVEL");
		SNATCH_RESERVE_ENEMY_COUNT = GlobalService.getInt("SNATCH_RESERVE_ENEMY_COUNT");
		SNATCH_RESERVE_ENEMY_DAY = GlobalService.getInt("SNATCH_RESERVE_ENEMY_DAY");
		SNATCH_ENEMY_DISPLAY_NUM = GlobalService.getInt("SNATCH_ENEMY_DISPLAY_NUM");
		SNATCH_ENEMY_LEAST_ROBOT_NUM = GlobalService.getInt("SNATCH_ENEMY_LEAST_ROBOT_NUM");
		SNTACH_ENEMY_REAL_ACTOR_PERCENT = GlobalService.getInt("SNTACH_ENEMY_REAL_ACTOR_PERCENT");

		SNATCH_EXCEED_LEVEL_PASS_BATTLE = GlobalService.getInt("SNATCH_EXCEED_LEVEL_PASS_BATTLE");
	}
	
	/**
	 * 特定角色是否碰到机器人
	 * @param actorLevel
	 * @return
	 */
	public static boolean isRobotGoldScope(int actorLevel) {
		return actorLevel >= SNATCH_ONLY_ROBOT_GOLD_MIN_LEVEL && actorLevel <= SNATCH_ONLY_ROBOT_GOLD_MAX_LEVEL;
	}
	
	/**
	 * 随机对手中是否命中真人
	 * @return
	 */
	public static boolean hitEnemyRealActor() {
		return RandomUtils.is100Hit(SNTACH_ENEMY_REAL_ACTOR_PERCENT);
	}
	
	/**
	 * 切换对手时至少出现机器人数量
	 * @return
	 */
	public static int getLeastEnemyRobotNum() {
		return SNATCH_ENEMY_LEAST_ROBOT_NUM > SNATCH_ENEMY_DISPLAY_NUM ? SNATCH_ENEMY_DISPLAY_NUM : SNATCH_ENEMY_LEAST_ROBOT_NUM;
	}
	


}
