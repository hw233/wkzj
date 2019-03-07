package com.jtang.gameserver.module.snatch.type;


public enum SnatchExchangeType {
	
	/**
	 * 普通物品
	 */
	GOODS(0),
	
	/**
	 * 装备
	 */
	EQUIP(1),
	
	/**
	 * 仙人魂魄
	 */
	HERO_SOUL(2),
	
	/**
	 * 装备碎片
	 */
	PIECE(3),
	
	/**
	 * 仙人
	 */
	HERO(4), 
	
	NONE(-1);
	
	private int type;
	
	private SnatchExchangeType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	public static SnatchExchangeType getType(int code) {
		 for(SnatchExchangeType type : SnatchExchangeType.values()) {
			 if(type.getType() == code) {
				 return type;
			 }
		 }
		return NONE;
	}

}
