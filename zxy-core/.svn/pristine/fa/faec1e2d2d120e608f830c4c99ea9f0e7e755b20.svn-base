package com.jtang.core.model;

/**
 * 奖励类型
 * (0:物品，1：装备，2：仙人魂魄 ，3：金币）
 * @author ludd
 *
 */
public enum RewardType {
	
	
	/**
	 * 物品
	 */
	GOODS(0),
	
	/**
	 * 装备
	 */
	EQUIP(1),
	
	/**
	 * 仙人魂魄
	 */
	HEROSOUL(2), 
	
	/**
	 * 金币
	 */
	GOLD(3),
	/**
	 * 英雄
	 */
	HERO(4),
	/**
	 * 点券 
	 */
	TICKET(5),
	/**
	 * 活力
	 */
	VIT(6),
	/**
	 * 精力
	 */
	ENERGY(7),
	NONE(-1);

	private int code;

	private RewardType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static RewardType getType(int code) {
		 for(RewardType type : RewardType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return NONE;
	}
}
