package com.jtang.gameserver.module.chat.type;

public enum SystemChatType {
	/**
	 * 装备类型
	 */
	EQUIP(1),

	/**
	 * 仙人类型
	 */
	HERO(2);

	private int code;

	private SystemChatType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
