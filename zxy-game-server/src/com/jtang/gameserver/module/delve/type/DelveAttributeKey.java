package com.jtang.gameserver.module.delve.type;

/**
 * 潜修室属性KEY
 * @author ludd
 *
 */
public enum DelveAttributeKey {

	/**
	 * 等级
	 */
	LEVEL(1);
	private byte code;
	
	private DelveAttributeKey(int code) {
		this.code = (byte)code;
	}
	public byte getCode() {
		return code;
	}
	
	public void setCode(byte code) {
		this.code = code;
	}
}

