package com.jtang.gameserver.module.user.type;

public enum RechargeType {
	
	/**
	 * 普通充值
	 */
	RECHARGE(1),
	
	/**
	 * 月卡
	 */
	MONTH_CARD(2),
	
	/**
	 * 终身卡
	 */
	LIFELONG_CARD(3),
	
	/**
	 * 年卡
	 */
	YEAR_CARD(4);
	
	private int id;
	
	private RechargeType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static RechargeType getType(int code) {
		 for(RechargeType type : RechargeType.values()) {
			 if(type.getId() == code) {
				 return type;
			 }
		 }
		return null;
	}
}
