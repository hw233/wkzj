package com.jtang.gameserver.module.delve.type;

public enum OneKeyDelveType {

	/**
	 * 一次潜修
	 */
	TYPE_1(1),
	
	/**
	 * 十次潜修
	 */
	TYPE_2(2),
	
	NONE(0);
	
	private int code;

	private OneKeyDelveType(int code) {
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
	
	public static OneKeyDelveType getType(int code) {
		 for(OneKeyDelveType type : OneKeyDelveType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return NONE;
	}
}
