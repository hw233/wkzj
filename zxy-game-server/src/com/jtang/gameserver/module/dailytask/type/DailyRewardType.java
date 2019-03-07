package com.jtang.gameserver.module.dailytask.type;

public enum DailyRewardType {
	/**
	 * 道具
	 */
	GOOS(0),
	/**
	 * 势力经验
	 */
	EXP(1),
	/**
	 * 活力
	 */
	VIT(2),
	/**
	 * 精力
	 */
	ENERGY(3),
	/**
	 * 金币
	 */
	GOLD(4),
	/**
	 * 点券
	 */
	TICKET(5),
	/**
	 * VIP等级变化元宝
	 */
	VIP_TICKET(6),
	/**
	 * 经验值（该掌教等级所需经验的百分比）
	 * 参数x1为掌教当前等级所需经验
	 * x1*0.5
	 */
	REPUTATION(7),
	/**
	 * 试炼洞试炼次数
	 */
	TRIALCAVE(8);
	private final int code;
	private DailyRewardType(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static DailyRewardType getByCode(int code) {
		for (DailyRewardType type : values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return null;
	}
}
