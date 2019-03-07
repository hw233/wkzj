package com.jtang.gameserver.module.user.type;

/**
 * 点券扣除类型
 * @author 0x737263
 *
 */
public enum TicketDecreaseType {
	
	/**
	 * 秘法堂升级
	 * id:当前等级  num:目标等级
	 */
	CABALA_UPGRADE(1),
	
	/**
	 * 潜修室升级
	 * id:当前等级  num:目标等级
	 */
	DELVE_UPGRADE(2),
	
	/**
	 * 吸灵室升级
	 * id:当前等级  num:目标等级
	 */
	VAMPIIR_UPGRADE(3),
	
	/**
	 * 聚仙室升级
	 * id:当前等级  num:目标等级
	 */
	RECRUIT_UPGRADE(4),
	
	/**
	 * 聚仙室招募
	 * id:聚仙阵类型  num:未用
	 */
	RECRUIT_RAND(5),
	
	/**
	 * 强化室升级
	 * id:当前等级  num:目标等级
	 */
	ENHANCED_UPGRADE(6),
	
	/**
	 * 阵型格子解锁
	 * id:未用 num:未用
	 */
	LINEUP_UNLOCK(7),
	
	/**
	 * 精炼室升级
	 * id:当前等级  num:目标等级
	 */
	REFINE_UPGRADE(8),
	
	/**
	 * 试炼洞升级
	 * id:当前等级  num:目标等级
	 * 
	 */
	@Deprecated
	TRIALCAV_UPGRADE(9),

	/**
	 * 抢夺失败
	 * id:未用 num:未用
	 */
	SNATCH_FAIL(10),
	
	/**
	 * 炼器宗师，合成装备
	 * id:未用 num:未用
	 */
	EQUIP_COMPOSE(11),
	
	/**
	 * 仙人合成
	 * id:未用 num:未用
	 */
	HERO_COMPOSE(12),
	
	/**
	 * 云游仙商
	 * id:物品id num:数量
	 * */
	SHOP_BUY(13),
	
	/**
	 * 活动购买
	 * id:未用 num:未用
	 */
	ACTIVE_BUY(14),
	/**
	 * 重生功能
	 */
	ACTOR_RESET(15),
	
	/**
	 * 管理平台扣除
	 */
	ADMIN(16),
	
	/**
	 * 天财地宝
	 */
	TREASURE(17), 
	/**
	 * 集众降魔攻打玩家
	 */
	DEMON_ATTACK_PLAYER(18),
	/**
	 * 集众降魔攻打boss
	 */
	DEMON_ATTACK_BOSS(19),
	/**
	 * 仙人重置
	 */
	HERO_RESET(20),
	/**
	 * 装备重置
	 */
	EQUIP_RESET(21), 
	/**
	 * 登天塔跳层
	 */
	BableSkip(22),
	/**
	 * 迷宫寻宝
	 */
	MAZE_TREASURE(23),
	/**
	 * 种植
	 */
	PLANT(24),
	/**
	 * 天宫探物
	 */
	WELKIN(25), 
	/**
	 * 掌教名称修改
	 */
	ACTOR_RENAME(26),
	/**
	 * 主界面补满精力
	 */
	FULL_ENERGY(27),
	/**
	 * 主界面补满活力
	 */
	FULL_VIT(28),
	/**
	 * 抢夺兑换刷新
	 */
	SNATCH_EXCHANGE_FLUSH(29),
	
	/**
	 * 主界面购买金币
	 */
	BUY_GOLD(30),
	
	/**
	 * 黑市商店刷新
	 */
	BLACK_SHOP_FLUSH(31), 
	
	/**
	 * 购买扫荡符
	 */
	BUY_FIGHT(32), 

	/**
	 * 试炼洞购买重置试炼
	 */
	BUY_TRIAL_CAVE_RESET(33),
	
	/**
	 * 补满排行榜挑战次数
	 */
	POWER_BUY(34), 
	
	/**
	 * vip商店购买
	 */
	VIP_SHOP(35), 
	
	/**
	 * 购买抢夺次数
	 */
	SNATCH_NUM(36), 
	
	/**
	 * 购买天梯战斗次数
	 */
	LADDER(37), 
	
	/**
	 * 云游商人购买
	 */
	TRADER(38), 
	
	/**
	 * 云游商人常驻
	 */
	TRADER_EVER(39), 
	
	/**
	 * 云游商人刷新商品
	 */
	TRADER_FLUSH(40), 
	
	/**
	 * 结婚,离婚消耗
	 */
	LOVE(41),
	
	/**
	 * 天神下凡
	 */
	DEITY_DESCEND(42),
	
	/**
	 * 神匠来袭
	 */
	CRAFTSMAN_BUILD(43),
	
	/**
	 * 邀请好友
	 */
	INVITE(44), 
	/**
	 * 欢乐摇奖
	 */
	ERNIE(45), 
	
	/**
	 * 排行榜商店刷新
	 */
	POWER_SHOP_FLUSH(46),
	
	/**
	 * 仙侣挑战补满挑战次数
	 */
	LOVE_FIGHT_NUM(47), 
	
	/**
	 * 仙侣商城购买
	 */
	LOVE_SHOP(48), 
	
	/**
	 * 仙侣商城刷新
	 */
	LOVE_SHOP_FLUSH(49), 
	
	/**
	 * 仙侣合作解锁boss
	 */
	LOVE_MONSTER(50), 
	
	/**
	 * 年兽
	 */
	BEAST(51),
	
	/**
	 * 仙侣合作刷新攻击次数
	 */
	LOVE_MONSTER_FLUSH(52), 
	
	/**
	 * 开启vip箱子
	 */
	VIP_BOX(53),
	
	/**
	 * 工会捐献
	 */
	UNION_DEVOTE(54);
	
	private int id;
	
	private TicketDecreaseType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
