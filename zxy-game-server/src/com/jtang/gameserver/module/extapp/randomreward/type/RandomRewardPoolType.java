package com.jtang.gameserver.module.extapp.randomreward.type;

public enum RandomRewardPoolType {
	/**
	 * 1.精炼石
	 */
	TYPE1(1),
	/**
	 * 2.潜修石
	 */
	TYPE2(2),
	/**
	 * 3.聚仙符
	 */
	TYPE3(3),
	/**
	 * 4.扫荡符
	 */
	TYPE4(4),
	/**
	 * 5.金币
	 */
	TYPE5(5),
	/**
	 * 6.元宝
	 */
	TYPE6(6), 
	
	NONE(0);
	
	private int code;

	private RandomRewardPoolType(int code) {
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
	
	public static RandomRewardPoolType getType(int code) {
		 for(RandomRewardPoolType type : RandomRewardPoolType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return NONE;
	}

}
