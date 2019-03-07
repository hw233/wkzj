package com.jtang.gameserver.module.refine.type;


public enum RefineType {
	
	/**
	 * 第一种方式
	 */
	TYPE_1(1),
	
	/**
	 * 第二种方式
	 */
	TYPE_2(2),
	
	/**
	 * 第三种方式
	 */
	TYPE_3(3),
	
	NONE(0);
	
	private int code;

	private RefineType(int code) {
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
	
	public static RefineType getType(int code) {
		 for(RefineType type : RefineType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return NONE;
	}
}
