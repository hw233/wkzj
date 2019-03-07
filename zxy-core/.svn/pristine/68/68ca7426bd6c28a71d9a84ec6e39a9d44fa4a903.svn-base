package com.jiatang.common.model;


/**
 * 角色的属性定义对象 负数代表是服务器专用,非负数是和客户端共有，不可以随便改
 * 带装备和基本属性时使用
 * 
 * @author vinceruan
 * 
 */
public enum AttackerAttributeKey {
	/** HP */
	HP(1, DataType.INT),

	/** 经验. */
	EXP(2, DataType.INT),

	/** 射程 */
	ATTACK_SCOPE(3, DataType.BYTE),

	/** 攻击力 */
	ATK(4, DataType.INT),

	/** 防御力 */
	DEFENSE(5, DataType.INT),

	/** 坐标 */
	POSITION(6, DataType.POSITION),

	/** 血量上限 */
	HP_MAX(7, DataType.INT),
	
	/** 阵营*/
	CAMP(8, DataType.BYTE),
	/** 定身 */
	IMMOBILIZE(9, DataType.BYTE),
	/**
	 * 伤害改变
	 */
	HERT_CHANGE(10, DataType.INT),

	// --------------------------以下是服务器专用属性----------------------------

	/** 不存在的属性 */
	NONE(-1, DataType.NONE),

	

	/** 增加普通攻击次数 */
	COMMON_ATTACK_TIMES(-4, DataType.BYTE),

	/** 内讧 */
	IN_FIGHTING(-5, DataType.BYTE),

	/** 闪避 */
	DODGE(-6, DataType.BYTE),	

	/** 武器ID */
	WEAPON_ID(-7, DataType.INT),

	/** 武器等级 */
	WEAPON_LEVEL(-8, DataType.INT),

	/** 防具id*/
	ARMOR_ID(-9, DataType.INT),
	
	/** 防具等级 */
	ARMOR_LEVEL(-10, DataType.INT), 
	
	/** 饰品id */
	ORNAMENTS_ID(-11, DataType.INT), 
	
	/** 饰品等级 */
	ORNAMENTS_LEVEL(-12, DataType.INT),
	
	/** 免疫 */
	IMMUNITY(-13, DataType.INT),
	
	/** 标记*/
	MARK(-14, DataType.INT),
	
	/** 内讧 并且伤害变更*/
	IN_FIGHTING_HEAT_CHANGE(-15, DataType.BYTE);
	

	private byte code;
	private DataType dataType;

	private AttackerAttributeKey(int code, DataType dataType) {
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

	public static AttackerAttributeKey getByCode(byte code) {
		for (AttackerAttributeKey key : AttackerAttributeKey.values()) {
			if (key.getCode() == code) {
				return key;
			}
		}
		return NONE;
	}
	
	public static Number toType(AttackerAttributeKey key, Number value){
		return null;
	}
}
