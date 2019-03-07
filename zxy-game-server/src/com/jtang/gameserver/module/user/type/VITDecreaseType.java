package com.jtang.gameserver.module.user.type;

/**
 * 活力消耗类型
 * @author 0x737263
 *
 */
public enum VITDecreaseType {
	
	/**
	 * 打故事
	 */
	STORY(1);
	
	private int id;
	
	private VITDecreaseType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
}
