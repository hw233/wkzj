package com.jtang.gameserver.module.extapp.vipbox.type;


public enum VipBoxType {
	/**
	 * 0.金币,
	 */
	GOLD(0),
	
	/**
	 * 1.元宝
	 */
	TICKET(1),
	
	/**
	 * 2.物品
	 */
	GOODS(2), 
	
	/**
	 * 3.装备
	 */
	EQUIP(3),
	/**
	 * 4.装备碎片
	 */
	FRAGMENT(4),
	/**
	 * 5.魂魄
	 */
	HEROSOUL(5),
	NONE(-1);

	private int code;

	private VipBoxType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static VipBoxType getType(int code) {
		 for(VipBoxType type : VipBoxType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return NONE;
	}
}
