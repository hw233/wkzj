package com.jtang.gameserver.module.equipdevelop.type;


/**
 * 分解类型枚举
 * @author hezh
 *
 */
public enum ConvertTypeEnum {
	
	/** 装备碎片*/
	PIECE(1),
	
	/** 装备*/
	EQUIP(2);
	
	private int code;
	
	private ConvertTypeEnum(int code){
		this.code = code;
	}
	
	public int getCode(){
		return code;
	}
	
	public static ConvertTypeEnum get(int code) {
		for (ConvertTypeEnum o : values()) {
			if (o.code == code) {
				return o;
			}
		}
		return null;
	}
}
