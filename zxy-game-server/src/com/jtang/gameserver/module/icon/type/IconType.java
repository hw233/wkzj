package com.jtang.gameserver.module.icon.type;

public enum IconType {

	/**
	 * 潜修解锁
	 */
	DELVE(1),
	
	/**
	 * 获取解锁
	 */
	ADD(2);
	
	private int code;

	private IconType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
