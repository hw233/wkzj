package com.jtang.gameserver.module.goods.type;

public enum BoxRewardType {
	
	/**
	 * 装备
	 */
	EQUIP(0),
	
	/**
	 * 装备碎片
	 */
	FRAGMENT(1),
	
	/**
	 * 物品
	 */
	GOODS(2),
	
	/**
	 * 金币
	 */
	GOLD(3);
	
	private int id;
	
	private BoxRewardType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

}
