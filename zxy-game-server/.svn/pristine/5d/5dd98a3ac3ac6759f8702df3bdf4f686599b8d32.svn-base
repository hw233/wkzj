package com.jtang.gameserver.module.adventures.shop.trader.type;


public enum ShopType {
	/**
	 * 云游商店
	 * */
	TYPE1(1);

//	/**
//	 * 黑市商店
//	 * */
//	TYPE2(2),
//	
//	/**
//	 * 珍宝阁
//	 * */
//	TYPE3(3);
	
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
		return TYPE1;
	}
}
