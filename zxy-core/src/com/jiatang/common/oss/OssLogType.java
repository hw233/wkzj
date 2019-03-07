package com.jiatang.common.oss;

/**
 * oss产出/消耗类型
 * @author 0x737263
 *
 */
public enum OssLogType {
	
	/**
	 * 产出
	 */
	ADD(1),
	
	/**
	 * 消耗
	 */
	DECREASE(2);
	
	private int id;
	
	private OssLogType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
}
