package com.jtang.gameserver.module.adventures.shop.trader.type;

public enum TraderShopType {

	/**
	 * 普通物品(精炼石、潜修石)
	 */
	GOODS(0),
	
	/**
	 * 装备碎片
	 */
	FRAGMENT(1),
	
	/**
	 * 其他物品(精魄、荡妖符、桃子、药水等)
	 */
	TARGET_GOODS(2),
	
	/**
	 * 魂魄
	 */
	SOUL(3),
	
	/**
	 * 装备
	 */
	EQUIPS(4);
	
	private int code;

	private TraderShopType(int code) {
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
	
	public static TraderShopType getType(int code) {
		 for(TraderShopType type : TraderShopType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return GOODS;
	}
	
	
}
