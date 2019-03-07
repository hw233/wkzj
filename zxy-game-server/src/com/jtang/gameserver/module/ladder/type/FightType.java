package com.jtang.gameserver.module.ladder.type;

public enum FightType {

	/**
	 * 赢
	 */
	WIN(1),
	
	/**
	 * 输
	 */
	LOST(2);
	
	public int code;
	
	private FightType(int code) {
		this.code = code;
	}
	
	public int getCode(){
		return code;
	}
}
