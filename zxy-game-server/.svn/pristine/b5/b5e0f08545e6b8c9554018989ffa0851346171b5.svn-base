package com.jtang.gameserver.module.user.type;


public enum DisableType {
	/**
	 * 玩家id
	 */
	ACTOR_ID(1),
	
	/**
	 * mac
	 */
	MAC(2),
	
	/**
	 * sim
	 */
	SIM(3),
	
	/**
	 * imei
	 */
	IMEI(4),
	
	/**
	 * ip
	 */
	IP(5);
	
	private int code;
	
	private DisableType(int code){
		this.code = code;
	}
	
	public int getCode(){
		return code;
	}
	
	public static DisableType getType(int code) {
		 for(DisableType type : DisableType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return null;
	}
}
