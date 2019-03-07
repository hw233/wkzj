package com.jtang.gameserver.module.treasure.type;

public enum MoveType {
	
	/**
	 * 左
	 */
	LEFT(1),
	
	/**
	 * 右
	 */
	RIGHT(2),
	
	/**
	 * 上
	 */
	UP(3),
	
	/**
	 * 下
	 */
	DOWN(4);
	
	private int code;

	private MoveType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
}
