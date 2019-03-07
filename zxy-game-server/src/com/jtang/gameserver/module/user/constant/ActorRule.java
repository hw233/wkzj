package com.jtang.gameserver.module.user.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

/**
 * 角色的默认规则
 * @author 0x737263
 *
 */
public class ActorRule {
	
	/**
	 * 创建时的默认等级
	 */
	public static int ACTOR_INIT_LEVEL;

	/**
	 * 角色创建时的默认金币
	 */
	public static int ACTOR_INIT_GOLD ;
	
	/**
	 * 角色创建时的默认点券数
	 */
	public static int ACTOR_INIT_TICKET;
	
	/**
	 * 创建角色时默认活力值
	 */
	public static int ACTOR_INIT_VIT;

	/**
	 * 创建角色时默认的精力值
	 */
	public static int ACTOR_INIT_ENERGY;
	
	/**
	 * 创建角色时默认声望
	 */
	public static int ACTOR_INIT_REPUTATION;
	
	/**
	 * 创建角色时默认的气势值
	 */
	public static int ACTOR_INIT_POWER;
	
	/**
	 * 活力定时恢复时间(秒)
	 */
	public static int ACTOR_VIT_FIXED_TIME;
	
	/**
	 * 每x分钟回复的活力值
	 */
	public static int ACTOR_X_MIN_ADD_VIT;
	
	/**
	 * 精力定时恢复时间(秒)
	 */
	public static int ACTOR_ENERGY_FIXED_TIME;

	/**
	 * 每x分钟回复的精力值
	 */
	public static int ACTOR_X_MIN_ADD_ENERGY;
	
	/**
	 * 新手引导的key最大值 10
	 */
	public static int ACTOR_GUIDE_MAX_KEY;
	
	/**
	 * 新手引导的value最大值  50
	 */
	public static int ACTOR_GUIDE_MAX_VALUE;
	
	/**
	 * 角色创建赠送的物品Id
	 */
	public static int ACTOR_CREATE_GIVE_GOODS_ID;
	
	/**
	 * 服务推送key长度
	 */
	public static int ACTOR_PUSH_KEY_LENGTH = 64;
	
	/**
	 * 精力超上限最大值
	 */
	public static int ACTOR_ENERGY_SUPER_MAX = 9999;
	
	/**
	 * 活力超上限最大值
	 */
	public static int ACTOR_VIT_SUPER_MAX = 9999;
	
	/**
	 * 修改掌教名字需要消耗的点券
	 */
	public static String RENAME_COST_TICKET;
	/**
	 * 修改掌教行呗消耗的点券表达式
	 */
	public static String RESEX_COST_TICKET;
	
	/**
	 * 购买金币增加的金币袋id
	 */
	public static int BUY_GOLD_NUM;
	
	/**
	 * 在线礼包领取最小等级限制
	 */
	public static int ONLINE_GIFTS_MIN_LEVEL;

	static {
		ACTOR_INIT_LEVEL = GlobalService.getInt("ACTOR_INIT_LEVEL");
		ACTOR_INIT_GOLD = GlobalService.getInt("ACTOR_INIT_GOLD");
		ACTOR_INIT_TICKET = GlobalService.getInt("ACTOR_INIT_TICKET");
		ACTOR_INIT_VIT = GlobalService.getInt("ACTOR_INIT_VIT");
		ACTOR_INIT_ENERGY = GlobalService.getInt("ACTOR_INIT_ENERGY");
		ACTOR_INIT_REPUTATION = GlobalService.getInt("ACTOR_INIT_REPUTATION");
		ACTOR_INIT_POWER = GlobalService.getInt("ACTOR_INIT_POWER");
		
		ACTOR_VIT_FIXED_TIME = GlobalService.getInt("ACTOR_VIT_FIXED_TIME") * 60;
		ACTOR_X_MIN_ADD_VIT = GlobalService.getInt("ACTOR_X_MIN_ADD_VIT");
		ACTOR_ENERGY_FIXED_TIME = GlobalService.getInt("ACTOR_ENERGY_FIXED_TIME") * 60;
		ACTOR_X_MIN_ADD_ENERGY = GlobalService.getInt("ACTOR_X_MIN_ADD_ENERGY");
		
		ACTOR_GUIDE_MAX_KEY = GlobalService.getInt("ACTOR_GUIDE_MAX_KEY");
		ACTOR_GUIDE_MAX_VALUE = GlobalService.getInt("ACTOR_GUIDE_MAX_VALUE");
		
		ACTOR_CREATE_GIVE_GOODS_ID = GlobalService.getInt("ACTOR_CREATE_GIVE_GOODS_ID");
		
		ACTOR_ENERGY_SUPER_MAX = GlobalService.getInt("ACTOR_ENERGY_SUPER_MAX");
		ACTOR_VIT_SUPER_MAX = GlobalService.getInt("ACTOR_VIT_SUPER_MAX");
		
		RENAME_COST_TICKET = GlobalService.get("RENAME_COST_TICKET");
		
		BUY_GOLD_NUM = GlobalService.getInt("BUY_GOLD_NUM");
		RESEX_COST_TICKET = GlobalService.get("RESEX_COST_TICKET");
		
		ONLINE_GIFTS_MIN_LEVEL = GlobalService.getInt("ONLINE_GIFTS_MIN_LEVEL");
	}
	
}
