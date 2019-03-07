package com.jtang.gameserver.module.praise.type;

public enum PraiseRewardType {
	/**
	 * 激活奖励
	 */
	ACTIVE_REWARD(0),
	/**
	 * 好评奖励
	 */
	COMMENT_REWARD(1);
	
	private final int type;
	private PraiseRewardType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static PraiseRewardType getByType(int type) {
		for (PraiseRewardType praiseRewardType : values()) {
			if (type == praiseRewardType.getType()) {
				return praiseRewardType;
			}
		}
		return null;
	}
}
