package com.jiatang.common.model;

/**
 * 用基本属性的时候使用
 * @author ludd
 *
 */
public enum HeroVOAttributeKey {
	/** 不存在的属性 */
	NONE(-1, DataType.NONE),

	/** HP */
	HP(1, DataType.INT),

	/** 经验. */
	EXP(2, DataType.INT),

	/** 射程 */
	ATTACK_SCOPE(3, DataType.INT),

	/** 攻击力 */
	ATK(4, DataType.INT),

	/** 防御力 */
	DEFENSE(5, DataType.INT),

	/** 级别 */
	LEVEL(6, DataType.INT),

	/** 可用的潜修次数 */
	AVAILABLE_DEVLE_COUNT(8, DataType.INT),

	/** 已经潜修的次数 */
	USED_DEVLE_COUNT(9, DataType.INT),

	/** 已经突破的次数 */
	BREAK_THROUGH_COUNT(10, DataType.INT),

	/** 被动技能列表 */
	PASSIVE_SKILL(11, DataType.INT_LIST),
	
	/** 是否允许重修 */
	ALLOW_REDELVE(12, DataType.INT),
	
	/** 主动技能  */
	SKILL_ID(13, DataType.INT),
	/** 消耗金币 */
	COST_GOLD(14, DataType.LONG),
	/** 消耗石头*/
	COST_STONE(15, DataType.INT);

	private byte code;
	private DataType dataType;

	private HeroVOAttributeKey(int code, DataType dataType) {
		this.code = (byte) code;
		this.dataType = dataType;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public static HeroVOAttributeKey getByCode(byte code) {
		for (HeroVOAttributeKey key : HeroVOAttributeKey.values()) {
			if (key.getCode() == code) {
				return key;
			}
		}
		return NONE;
	}
}
