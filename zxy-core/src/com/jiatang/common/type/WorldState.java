package com.jiatang.common.type;

public enum WorldState {
	/**
	 * 1.维护状态（允许ip列表访问）
	 */
	MAINTAIN(1),
	/**
	 * 0.正常状态（所有人可访问）
	 */
	OPEN(0),
	/**
	 * 2,关闭状态（所有人不可访问
	 */
	CLOSE(2);
	private final int type;
	private WorldState(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	
	public static WorldState getByType(int type) {
		for (WorldState worldState : values()) {
			if (type == worldState.getType()) {
				return worldState;
			}
		}
		return null;
	}
}
