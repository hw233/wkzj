package com.jtang.gameserver.module.luckstar.type;

public enum LuckStarRewardType {
	/**
	 * 物品
	 */
	GOODS(0),

	/**
	 * 金币
	 */
	GOLD(1),

	/**
	 * 补满星数
	 */
	LUCKSTAR(2),

	/**
	 * 装备
	 */
	EQUIP(3),

	/**
	 * 仙人魂魄
	 */
	HEROSOUL(4),

	/**
	 * 点券
	 */
	TICKET(5);

	private int code;

	private LuckStarRewardType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static LuckStarRewardType getType(int code) {
		for (LuckStarRewardType type : LuckStarRewardType.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return null;
	}

}
