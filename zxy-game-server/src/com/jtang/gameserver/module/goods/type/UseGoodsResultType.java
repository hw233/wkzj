package com.jtang.gameserver.module.goods.type;
/**
 * 使用物品产生结果id类型
 * @author ludd
 *
 */
public enum UseGoodsResultType {

	/**
	 *  物品
	 */
	GOODS(1),
	/**
	 * 装备
	 */
	EQUIP(2),
	/**
	 * 英雄魂魄
	 */
	HERO_SOUL(3),
	/**
	 * 英雄
	 */
	HERO(4);
	
	private final byte type;
	private UseGoodsResultType(int type) {
		this.type = (byte) type;
	}
	public byte getType() {
		return type;
	}
	
	
}
