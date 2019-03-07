package com.jtang.gameserver.module.notify.type;
/**
 * 0为假，1为真，比如是否领取，0为未领取，1为已领取；以及是否通知盟友，0没有通知，1为已通知
 * @author pengzy
 *
 */
public enum BooleanType {

	FALSE(0),
	
	TRUE(1);
	
	private byte code;
	
	private BooleanType(int code){
		this.code = (byte)code;
	}
	
	public byte getCode(){
		return code;
	}
	
	public String toString() {
		return String.valueOf(this.code);
	}
	
	public static BooleanType get(int code) {
		for (BooleanType type : BooleanType.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return BooleanType.FALSE;
	}
}
