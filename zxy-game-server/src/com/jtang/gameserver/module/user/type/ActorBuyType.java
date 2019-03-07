package com.jtang.gameserver.module.user.type;


public enum ActorBuyType {
	
	/**
	 * 活力
	 */
	VIT(0),
	
	/**
	 * 精力
	 */
	ENERGY(1),
	
	/**
	 * 金币
	 */
	GOLD(2);
	
	private int code;
	
	private ActorBuyType(int code){
		this.code = code;
	}
	
	public int getCode(){
		return code;
	}
	
	public static ActorBuyType getType(int code) {
		 for(ActorBuyType type : ActorBuyType.values()) {
			 if(type.getCode() == code) {
				 return type;
			 }
		 }
		return null;
	}
}
