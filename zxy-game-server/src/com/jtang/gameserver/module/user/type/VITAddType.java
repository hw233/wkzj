package com.jtang.gameserver.module.user.type;

/**
 * 活力增加类型
 * @author 0x737263
 *
 */
public enum VITAddType {
	
	/**
	 * 系统定时增加
	 */
	FIXTIME(1),
	
	/**
	 * 角色升级
	 */
	ACTOR_UPGRADE(2),
	
	/**
	 * 使用物品增加
	 */
	USE_GOODS(3),
	
	/**
	 * 福神眷顾领取
	 */
	FAVOR(4),
	
	/**
	 * 云游仙商购买补满
	 */
	SHOP_BUY(5),
	
	/**
	 * 授仙录达成
	 */
	ACHIEVE(6),
	
	/**
	 * 抢夺奖励
	 */
	SNATCH_AWARD(7),
	
	/**
	 * 任务奖励
	 */
	TASK_AWARD(8),
	
	/**
	 * 故事奖励
	 */
	STORY_REWARD(9),
	
	/**
	 * 补给领取
	 */
	SUPPLY(10),
	
	/**
	 * 主界面补满活力
	 */
	FULL_VIT(11);
	
	private int id;
	
	private VITAddType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
}
