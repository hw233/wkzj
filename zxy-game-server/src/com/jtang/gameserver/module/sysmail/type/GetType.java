package com.jtang.gameserver.module.sysmail.type;

public enum GetType {

	/**
	 * 未领取
	 */
	NOT_GET(0),
	
	/**
	 * 已领取
	 */
	GET(1);
	
	private int code;

	private GetType(int code) {
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
}
