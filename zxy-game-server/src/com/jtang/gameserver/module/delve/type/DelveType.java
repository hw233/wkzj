package com.jtang.gameserver.module.delve.type;


public enum DelveType {
	
	/**
	 * 潜修类型1
	 */
	TYPE_1(1),
	
	/**
	 * 潜修类型2
	 */
	TYPE_2(2),
	
	/**
	 * 潜修类型3
	 */
	TYPE_3(3),
	
	NONE(0);
	
	private int code;

	private DelveType(int code) {
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
	
	public static DelveType getType(int code) {
		 for(DelveType type : DelveType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return NONE;
	}
}
