package com.jtang.gameserver.module.user.type;

import com.jiatang.common.model.DataType;

/**
 * 角色同步属性key
 * @author 0x737263
 *
 */
public enum ActorAttributeKey {
	
	/**
	 * 等级
	 */
	LEVEL(1, DataType.INT),
	
	/**
	 * 声望
	 */
	REPUTATION(2, DataType.LONG),
	
	/**
	 * 金币
	 */
	GOLD(3, DataType.LONG),
	
	/**
	 *点券 
	 */
	TICKET(4, DataType.INT),
	
	/**
	 * 精力
	 */
	ENERGY(5, DataType.INT),
	
	/**
	 * 最大精力
	 */
	MAXENERGY(6, DataType.INT),
	
	/**
	 * 活力
	 */
	VIT(7, DataType.INT),
	
	/**
	 * 最大活力
	 */
	MAXVIT(8, DataType.INT),
	
	/**
	 * 气势 值
	 */
	MORALE(9, DataType.INT),
	/**
	 * VIP 等级
	 */
	VIP_LEVEL(10, DataType.INT),
	/**
	 * 总充值点券数
	 */
	TOTAL_TICKET(11, DataType.INT),
	
	/**
	 * 精力倒计时(秒)
	 */
	ENERGY_COUNT_DOWN(12, DataType.INT),
	
	/**
	 * 活力倒计时(秒)
	 */
	VIT_COUNT_DOWN(13, DataType.INT),
	
	/**
	 * 阵容首仙人
	 */
	LINEUP_FIRST_HERO(14,DataType.INT);
	
	private byte code;
	private DataType type;
	
	private ActorAttributeKey(int code, DataType dataType) {
		this.code = (byte)code;
		this.type = dataType;
	}
	
	public byte getCode() {
		return code;
	}
	
	public void setCode(byte code) {
		this.code = code;
	}

	public static ActorAttributeKey getByCode(byte code) {
		for (ActorAttributeKey key : values()) {
			if (code == key.code){
				return key;
			}
		}
		return null;
	}
	
	public DataType getType() {
		return type;
	}
	
}
