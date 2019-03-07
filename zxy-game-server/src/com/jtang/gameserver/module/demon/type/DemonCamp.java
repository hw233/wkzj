package com.jtang.gameserver.module.demon.type;
/**
 * 集众降魔阵营
 * @author ludd
 *
 */
public enum DemonCamp {
	/**
	 * 全部阵营
	 */
	ALL_CAMP(0),
	/**
	 * 红方阵营
	 */
	RED_CAMP(1),
	/**
	 * 蓝方阵营
	 */
	BLUE_CAMP(2);
	private final int code;
	private DemonCamp(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static DemonCamp getByCode(int code) {
		for (DemonCamp demonCamp : values()) {
			if (demonCamp.getCode() == code) {
				return demonCamp;
			}
		}
		return ALL_CAMP;
	}
}
