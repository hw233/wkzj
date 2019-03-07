package com.jtang.gameserver.module.adventures.achievement.type;

/**
 * 达成类型枚举
 * @author pengzy
 *
 */
public enum AchieveType {
	
	/**
	 * 
	 */
	NONE(0),
	
	/**
	 * 掌教等级达到X成就
	 * 达成条件格式: conditions="等级"
	 */
	ACTOR_LEVEL(1),
	
	/**
	 * 登天塔层数达到X成就
	 * 达成条件格式: conditions="层数"
	 */
	BABLE_FLOOR(2),
	
	/**
	 * 点券消耗达到X
	 * 达成条件格式: conditions="点券数"
	 */
	TICKETS_CONSUME(3),
	
	/**
	 * 充值达到X
	 * 达成条件格式: conditions="点券数"
	 */
	TICKETS_RECHARGE(4),
	
	/**
	 * 累计得到过X品质的武器X数量
	 * 达成条件格式: conditions="品质_数量"
	 */
	WEAPON(5),
	
	/**
	 * 累计得到过X品质的防具X数量
	 * 达成条件格式: conditions="品质_数量"
	 */
	ARMOR(6),
	
	/**
	 * 累计得到过X品质的饰品X数量
	 * 达成条件格式: conditions="品质_数量"
	 */
	DECORATION(7),
	
	/**
	 * 累计得到过X品质仙人X数量
	 * 达成条件格式: conditions="品质_数量"
	 */
	HERO_SUM(8),
	
	/**
	 * 精炼X品质的装备X次
	 * 达成条件格式: conditions="品质_次数"
	 */
	EQUIP_REFINE(9),
	
	/**
	 * 故事X章X关卡X星X次
	 * 达成条件格式: conditions="战场Id_星级_次数"
	 */
	STORY_PASSED(10),
	
	/**
	 * 气势达到
	 * 达成条件格式: conditions="气势值"
	 */
	MORALE(11),
	
	/**
	 * 装备强化等级达到
	 * 达成条件格式: conditions="等级"
	 */
	EQUIP_ENHANCED_LEVEL(12),
	
	/**
	 * 任意仙人升到X级
	 * 达成条件格式: conditions="仙人等级"
	 */
	HERO_LEVEL(13),
	
	/**
	 * 精力上限达到X
	 * 达成条件格式: conditions="上限值"
	 */
	ENERGY_LIMIT(14),
	
	/**
	 * 活力上限达到X
	 * 达成条件格式: conditions="上限值"
	 */
	VIT_LIMIT(15),
	
	/**
	 * 单次抢夺金币X
	 * 达成条件格式: conditions="抢夺金币数"
	 */
	SNATCH_GOLD(16),
	
	/**
	 * 修改名字
	 * TODO 这个有问题
	 */
	NAME_CHANGE(17),
	
	/**
	 * 抢夺X次
	 * 达成条件格式: conditions="累计次数"
	 */
	SNATCH_NUM(18),
	
	/**
	 * 盟友数量达x个
	 * 达成条件格式: conditions="盟友数量 "
	 */
	ALLY_NUM(19),
	
	/**
	 * 精练X次
	 * 达成条件格式: conditions="次数"
	 */
	REFINE_NUM(20),
	
	/**
	 * 抢夺积分达到X
	 * 达成条件格式: conditions="积分值"
	 */
	SNATCH_SCORE(21),
	
	/**
	 * 公共聊天次数
	 * 达成条件格式: conditions="聊天次数"
	 */
	CHAT_NUM(22),
	
	/**
	 * 云游仙商购买shopId物品x次
	 * 达成条件格式: conditions="shopId_次数"
	 */
	SHOP_BUY(23),
	
	/**
	 * 连续登陆达到x天
	 * 达成条件格式: conditions="天数"
	 */
	CONTINUE_LOGIN(24),
	
	/**
	 * 登上X难度的塔,到达X层数的 成就
	 * 达成条件格式: conditions="难度(1.容易  2.普通  3.困难)_层数"
	 */	
	BABLE_DIFFICULTY_FLOOR(25),

	/**
	 * 最强势力的排名变更达成 ，当角色等级大于 等于x级,并且排名小于等于 x名则达成
	 * 达成条件格式：conditions="角色等级_名次"
	 */
	POWER_TOP_RANK(26),
	
	/**
	 * 潜修X次
	 * 达成条件格式: conditions="次数"
	 */
	DELVE_NUM(27),
	/**
	 * 试练洞次数
	 * 达成条件格式: conditions="次数"
	 */
	TRIALCAVE(29),
	/**
	 * 阵容格子解锁
	 * 达成条件格式:conditions="第几个格子"
	 */
	LINEUP_UNLOCK(30),
	/**
	 * 排行榜战斗
	 * 达成条件格式:conditions="战斗次数"
	 */
	POWER_FIGHT(31);
	
	
	private byte id;
	
	private AchieveType(int id) {
		this.id = (byte) id;
	}
	
	public byte getId() {
		return id;
	}
	
	/**
	 * 获取枚举类型
	 * @param id
	 * @return
	 */
	public static AchieveType getType(int id) {
		for (AchieveType type : AchieveType.values()) {
			if (type.getId() == id) {
				return type;
			}
		}
		return AchieveType.NONE;
	}
	
}
