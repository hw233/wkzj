package com.jtang.gameserver.module.ally.type;

import com.jiatang.common.model.DataType;

public enum AllyAttributeKey {

	/**
	 * 盟友ID
	 */
	ACTOR_ID(1, DataType.LONG),
	/**
	 * 盟友名字
	 */
	NAME(2, DataType.STRING),
	/**
	 * 盟友等级
	 */
	LEVEL(3, DataType.INT),
	/**
	 * 盟友是否在线
	 */
	IS_ONLINE(4, DataType.BYTE),
	/**
	 * 战斗时间
	 */
	FIGHT_TIME(5, DataType.INT),
	/**
	 * 已切磋次数
	 */
	FIGHT_NUM(6, DataType.INT),
	/**
	 * 盟主失败次数
	 */
	FAIL_NUM(7, DataType.INT), 
	/**
	 * 盟主胜利次数
	 */
	WIN_NUM(8, DataType.INT);
	
	private byte code;
	private DataType type;
	private AllyAttributeKey(int code, DataType type){
		this.code = (byte)code;
		this.type = type;
	}
	public byte getCode(){
		return code;
	}
	
	public DataType getDataType(){
		return type;
	}
}
