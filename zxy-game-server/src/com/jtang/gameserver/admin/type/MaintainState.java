package com.jtang.gameserver.admin.type;
/**
 *  服务器状态
 * @author ludd
 *
 */
public enum MaintainState {
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
	private MaintainState(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	
	public static MaintainState getByType(int type) {
		for (MaintainState maintainState : values()) {
			if (type == maintainState.getType()) {
				return maintainState;
			}
		}
		return null;
	}
}
