package com.jtang.gameserver.module.notify.type;

public enum AttackType {
	/**
	 * 主动
	 */
	ATTACKING(1),
	/**
	 * 被动
	 */
	ATTACKED(2);
	
	private byte code;
	
	private AttackType(int code){
		this.code = (byte)code;
	}
	
	public byte getCode(){
		return code;
	}
	
	public String toString() {
		return String.valueOf(this.code);
	}
}
