package com.jtang.gameserver.module.story.type;

public enum StoryRewardType {

	/**
	 * 通关奖励
	 */
	CROSS_REWARD(1),
	/**
	 * 二星奖励
	 */
	TWO_STAR_REWARD(2),
	/**
	 * 三星奖励
	 */
	THREE_STAR_REWARD(3);
	private final int type;
	private StoryRewardType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static StoryRewardType getByType(int type) {
		for (StoryRewardType storyRewardType : values()) {
			if (storyRewardType.getType() == type) {
				return storyRewardType;
			}
		}
		return null;
	}
}
