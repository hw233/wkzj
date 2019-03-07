package com.jtang.gameserver.module.adventures.shop.shop.type;

public enum ShopGoodsType {
	/**
	 * 活力
	 * */
	VIT(1),
	
	/**
	 * 精力
	 * */
	ENERGY(2),
	
	/**
	 * 金币
	 * */
	GOLD(10);
	
	private int code;

	private ShopGoodsType(int code) {
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
	
	public static ShopGoodsType getType(int code) {
		 for(ShopGoodsType type : ShopGoodsType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return null;
	}
}
