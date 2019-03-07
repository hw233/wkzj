package com.jtang.gameserver.module.adventures.shop.trader.type;

public enum TraderConditionType {
	
	/**
	 * 闯关
	 */
	BATTLE(1),
	
	/**
	 * 精炼
	 */
	REFINE(2),
	
	/**
	 * 强化
	 */
	ENHANCED(3),
	
	/**
	 * 潜修
	 */
	DELVE(4),
	
	/**
	 * 升级
	 */
	UPLEVEL(5),
	
	/**
	 * 登天塔
	 */
	BABLE(6),
	
	/**
	 * 充值
	 */
	RECHARGE(7),
	
	NONE(0);
	
	private int code;

	private TraderConditionType(int code) {
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
	
	public static TraderConditionType getType(int code) {
		 for(TraderConditionType type : TraderConditionType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return NONE;
	}
}
