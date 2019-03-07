package com.jtang.gameserver.module.extapp.welkin.type;


public enum WelkinType {
	NONE(0),
	
	TYPE_1(1),
	
	TYPE_2(2),
	
	TPE_3(3);
	
	private int code;

	private WelkinType(int code) {
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
	
	public static WelkinType getType(int code) {
		 for(WelkinType type : WelkinType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return NONE;
	}
}
