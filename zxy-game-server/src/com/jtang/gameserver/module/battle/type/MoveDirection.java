package com.jtang.gameserver.module.battle.type;

/**
 * 移动方向.(根据仙人面向)
 * @author vinceruan
 *
 */
public enum MoveDirection {
	
	/**
	 * 前
	 */
	FORWARD,
	
	/**
	 * 后
	 */
	BACKWARD,
	
	/**
	 * 左
	 */
	LEFT,
	
	/**
	 * 右
	 */
	RIGHT;
	
	/**
	 * 获取相反的方向
	 * @return
	 */
	public MoveDirection getOppositeDirection() {
		switch (this) {
		case FORWARD:
			return BACKWARD;
		case BACKWARD:
			return FORWARD;
		case LEFT:
			return RIGHT;
		default:
			return LEFT;
		}
	}
}
