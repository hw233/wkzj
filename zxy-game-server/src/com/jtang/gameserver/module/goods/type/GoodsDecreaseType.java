package com.jtang.gameserver.module.goods.type;

/**
 * 活力消耗类型
 * @author 0x737263
 *
 */
public enum GoodsDecreaseType {
	
	/**
	 * 一般物品  (通常是一些直接点击即可用的)
	 */
	BASE(1),
	
	/**
	 * 道具物品   各种石头，不能直接点击使用的
	 */
	PROP_BASE(2),
	
	/**
	 * 碎片     各种碎片（武防饰)
	 */
	PIECE(3),
	
	/**
	 * 宝箱     金银铜
	 */
	TREASURE_BOX(4),

	/**
	 * 礼包     vip,新手等各种礼包换多个物品的
	 */
	GIFT_BAG(5),
	
	/**
	 * 货币     金币、点券物品包
	 */
	CURRENCY(6),
	
	/**
	 * 管理员平台扣除
	 */
	ADMIN(7), 
	
	/**
	 * 活动扣除
	 */
	APP_DECREASE(8), 
	
	/**
	 * 吸灵室消耗
	 */
	VAMPIIR_USE(9),
	
	/**
	 * 迷宫兑换
	 */
	TREASURE_DECREASE(10),
	
	/**
	 * 抢夺兑换刷新
	 */
	SNATCH_EXCHANGE_FLUSH(11), 
	
	/**
	 * 黑市商店刷新
	 */
	BLACK_SHOP_FLUSH(12), 
	/**
	 * 使用扫荡符
	 */
	STORY_FIGHT(13),
	/**
	 * 出售
	 */
	GOODS_SELL(14), 
	/**
	 * 登天塔使用
	 */
	BABLE(15), 
	/**
	 * 轮回熔炉兑换
	 */
	SMELT(16), 
	/**
	 * 结婚消耗
	 */
	LOVE(17),
	/**
	 * 云游商人刷新
	 */
	TRADER_FLUSH(18), 
	
	/**
	 *欢乐摇奖
	 */
	ERNIE(19),
	
	/**
	 * 封神榜商店兑换
	 */
	POWER_SHOP(20), 
	
	/**
	 * 封神榜商店刷新
	 */
	POWER_SHOP_FLUSH(21),
	
	/**
	 * 仙侣商城
	 */
	LOVE_SHOP(22), 
	/**
	 * 仙侣合作解锁boss
	 */
	LOVE_MONSTER(23), 
	
	/**
	 * 开启vip箱子
	 */
	VIP_BOX(24),
	
	/** 装备、装备碎片提炼消耗*/
	EQUIP_CONVERT(25),
	
	;
	
	
	
	private int id;
	
	private GoodsDecreaseType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
}
