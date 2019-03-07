package com.jtang.gameserver.module.extapp.randomreward.type;


public enum RandomRewardType {

	/**
	 * 小兵
	 */
	TYPE1(1),
	
	/**
	 * 女人
	 */
	TYPE2(2),
	
	/**
	 * 铁匠
	 */
	TYPE3(3), 
	
	NONE(0);
	
	private int code;

	private RandomRewardType(int code) {
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
	
	public static RandomRewardType getType(int code) {
		 for(RandomRewardType type : RandomRewardType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return NONE;
	}
}
