package com.jtang.gameserver.component.event;

public interface EventKey {
	
	/** 故事模块的战斗事件 */
	String STORY_BATTLE = "STORY_BATTLE";
	
	/** 城战 */
	String CITY_WAR_BATTLE = "CITY_WAR_BATTLE";
	
	/** 通天塔战斗事件 */
	String BABLE_BATTLE = "BABLE_BATTLE";
	
	/** 抢夺模块的战斗事件 */
	String SNATCH_BATTLE = "SNATCH_BATTLE";
	
	/** 试炼洞的战斗事件 */
	String TRIAL_BATTLE = "TRIAL_BATTLE";
	
	/** 切磋战斗事件 */
	String LEARN_BATTLE = "LEARN_BATTLE";
	
	/** 上古洞府战斗事件 */
	String HOLE_BATTLE = "HOLE_BATTLE";
	
	/** 迷宫寻宝战斗事件 */
	String TREASURE_BATTLE = "TREASURE_BATTLE";
	
	/** 角色升级 事件 */
	String ACTOR_LEVEL_UP = "ACTOR_LEVEL_UP";

	/** 英雄增加事件*/
	String ADD_HERO = "ADD_HERO";
	
	/** 阵型格子解锁*/
	String LINEUP_UNLOCK = "LINEUP_UNLOCK";
	
	/** 故事通关 */
	String STORY_PASSED = "STORY_PASSED";
	
	/** 最强势力排行的挑战战斗 */
	String POWER_BATTLE = "POWER_BATTLE";
	
	/** 删除盟友事件*/
	String REMOVE_ALLY = "REMOVE_ALLY";
	
	/** 添加盟友事件*/
	String ADD_ALLY = "ADD_ALLY";
	
	/** 登塔成功事件*/
	String BABLE_SUCESS = "BABLE_SUCESS";
	
	/** 抢夺结果 */
	String SNATCH_RESULT = "SNATCH_RESULT";
	
	/** vip等级变化 */
	String VIP_LEVEL_CHANGE = "VIP_LEVEL_CHANGE";
	
	/** 使用点券 */
	String TICKETS_USE = "TICKETS_USE";
	
	/** 充值*/
	String TICKETS_RECHARGE = "TICKETS_RECHARGE";
	
	/** 添加装备*/
	String ADD_EQUIP = "ADD_EQUIP";
	
	/** 精炼装备*/
	String EQUIP_REFINED = "EQUIP_REFINED";
	
	/** 气势增长事件*/
	String MORALE_INCREASE = "MORALE_INCREASE";
	
	/** 装备强化事件*/
	String EQUIP_ENHANCED = "EQUIP_ENHANCED";
	
	/** 活力上限增加*/
	String VIT_LIMIT = "VIT_LIMIT";
	
	/** 精力上限增加*/
	String ENERGY_LIMIT = "ENERGY_MAX_INCREASE";
	
	/** 修改名字事件*/
	String NAME_CHANGED = "NAME_CHANGED";
	
	/** 登天塔次数事件*/
	String BABLE_BATTLE_TIMES = "BABLE_BATTLE_TIMES";
	
	/** 聊天事件 */
	String CHAT = "CHAT";
	
	/** 格子上装备改变事件*/
	String GRID_EQUIP_CHANGE = "GRID_EQUIP_CHANGE";
	
	/** 阵形变化事件*/
	String LINE_UP_CHANGE = "LINE_UP_CHANGE";
	
	/** 云游仙商购买物品后事件*/
	String SHOP_BUY = "SHOP_BUY";
	
	/** 开启宝箱事件 */
	String OPEN_BOX = "EVENT_BOX";
	
	/** 英雄属性变更事件*/
	String HERO_ATTRIBUTE_CHANGE = "HERO_ATTRIBUTE_CHANGE";
	
	/** 仙人升级事件*/
	String HERO_LEVEL_UP = "HERO_LEVEL_UP";
	
	/** 连续登陆事件,当登陆天数有变更的时候抛出 */
	String CONTINUE_LOGIN = "CONTINUE_LOGIN";
	
	/** 最强势力排名变更 */
	String POWER_RANK_CHANGE = "POWER_RANK_CHANGE";
	
	/** 跨服战斗 */
	String CROSS_BATTLE = "CROSS_BATTLE";
	
	/** 聚仙事件*/
	String RECRUIT = "RECRUIT";
	
	/** 送礼事件*/
	String GIVE_GIFT = "GIVE_GIFT";
	
	/** 收礼事件*/
	String RECEIVE_GIFT = "RECEIVE_GIFT";
	
	/** 切磋 */
	String ALLAY_PK = "ALLAY_PK";

	/** 仙人潜修*/
	String HERO_DELVE = "HERO_DELVE";

	/** 洞府过关*/
	String HOLE_BATTLE_RESULT = "HOLE_BATTLE_RESULT";
	/** 试练洞*/
	String TRIAL_BATTLE_RESULT = "TRIAL_BATTLE_RESULT";
	
	/** 主界面购买金币 */
	String ACTOR_BUY_GOLD = "ACTOR_BUY_GOLD";
	
	/** 排行榜挑战事件*/
	String POWER_BATTLE_RESULT = "POWER_BATTLE_RESULT";

	/** 天梯战斗 */
	String LADDER = "LADDER";
	
	/** 开启福神宝箱事件*/
	String OPEN_VIP_BOX = "OPEN_VIP_BOX";
}
