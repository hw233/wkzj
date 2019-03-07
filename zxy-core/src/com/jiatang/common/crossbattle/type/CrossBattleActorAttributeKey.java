package com.jiatang.common.crossbattle.type;

import com.jiatang.common.model.DataType;


public enum CrossBattleActorAttributeKey {
	/**
	 * 血
	 */
	HP(1, DataType.INT),

	/**
	 * 连续杀人数
	 */
	CONTINUE_KILL_NUM(2, DataType.INT),
	/**
	 * 杀人数
	 */
	KILL_NUM(3, DataType.INT),
	/**
	 * 总伤害量
	 */
	TOTAL_HURT(4, DataType.LONG),
	/**
	 * 攻击时间
	 */
	ATTACK_TIME(5, DataType.INT),
	/**
	 * 死亡时间
	 */
	DEAD_TIME(6, DataType.INT),
	/**
	 * 复活时间
	 */
	REVIVE_TIME(7,DataType.INT);
	private final byte code;
	private final DataType dataType;
	
	private CrossBattleActorAttributeKey(int code, DataType dataType) {
		this.code = (byte) code;
		this.dataType = dataType;
	}
	public byte getCode() {
		return code;
	}
	public static CrossBattleActorAttributeKey getByCode(byte code) {
		for (CrossBattleActorAttributeKey key : CrossBattleActorAttributeKey.values()) {
			if (key.getCode() == code) {
				return key;
			}
		}
		return null;
	}
	
	public DataType getDataType() {
		return dataType;
	}

}
