package com.jtang.gameserver.module.applog.type;


public enum LogType {
	
	NONE(0),
	
	/**
	 * 天宫探物
	 */
	WELKIN(1); 
	
	private int code;

	private LogType(int code) {
		this.code =  code;
	}

	public int getCode() {
		return code;
	}
	
	public static LogType getType(int code) {
		 for(LogType type : LogType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return NONE;
	}

}
