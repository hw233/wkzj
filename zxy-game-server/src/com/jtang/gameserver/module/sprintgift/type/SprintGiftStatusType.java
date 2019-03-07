package com.jtang.gameserver.module.sprintgift.type;

/**
 * 冲级礼包领取状态
 * 1 已领取 （不发）  2  已达到未领取  3 未达到 
 * @author ligang
 *
 */
public enum SprintGiftStatusType {

	/**
	 * 已经领取
	 */
	HAD_RECEIVED(1),

	/**
	 * 已达到未领取  
	 */
	CAN_RECEIVE(2),
	
	/**
	 * 未达到 
	 */
	DO_NOT_RECEIVE(3);
	
	private final int type;
	private SprintGiftStatusType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static SprintGiftStatusType getByType(int type) {
		for (SprintGiftStatusType sprintGiftStatusType : values()) {
			if (sprintGiftStatusType.getType() == type) {
				return sprintGiftStatusType;
			}
		}
		return null;
	}
}
