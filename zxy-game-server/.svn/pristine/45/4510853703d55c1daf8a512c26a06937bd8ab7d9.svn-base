package com.jtang.gameserver.module.adventures.shop.shop.type;



public enum ShopType {
	
	NONE(-1),
	/**
	 * 物品
	 * */
	ITEM(0),

	/**
	 * 非物品
	 * */
	GOODS(1),
	
	/**
	 * 装备
	 * */
	EQUIP(2);
	
	private int code;

	private ShopType(int code) {
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
	
	public static ShopType getType(int code) {
		 for(ShopType type : ShopType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return NONE;
	}
}
