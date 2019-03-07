package com.jtang.gameserver.module.recruit.type;


public enum RecruitRewardType {
	/**
	 * 物品
	 */
	GOODS(0),
	
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
	HERO(1),
	/**
	 * 武器碎片
	 */
	EQUIP_ATK_ELMENT(7),
	/**
	 * 防具碎片
	 */
	EQUIP_DEF_ELMENT(8),
	/**
	 * 饰品碎片
	 */
	EQUIP_DEC_ELMENT(9),
	/**
	 * 武器
	 */
	EQUIP_ATK(4),
	/**
	 * 防具
	 */
	EQUIP_DEF(5),
	/**
	 * 饰品
	 */
	EQUIP_DEC(6);
	
	private int code;

	private RecruitRewardType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static RecruitRewardType getType(int code) {
		 for(RecruitRewardType type : RecruitRewardType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return null;
	}
}
