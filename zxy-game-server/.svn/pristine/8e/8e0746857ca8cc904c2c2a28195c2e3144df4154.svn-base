package com.jtang.gameserver.module.adventures.vipactivity.type;

public enum VipLevelType {
	
	NONE(0),
	
	VIP1(1),
	
	VIP2(2),
	
	VIP3(3),
	
	VIP4(4),
	
	VIP5(5),
	
	VIP6(6),
	
	VIP7(7),
	
	VIP8(8),
	
	VIP9(9),
	
	VIP10(10),
	
	VIP11(11),
	
	VIP12(12),
	
	VIP13(13);
	
	
	private int code;
	
	private VipLevelType(int code){
		this.code = code;
	}
	
	public int getCode(){
		return code;
	}
	
	public static VipLevelType get(int code){
		for(VipLevelType type : VipLevelType.values()){
			if(type.getCode() == code){
				return type;
			}
		}
		return NONE;
	}
}
