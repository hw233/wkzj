package com.jtang.gameserver.module.extapp.beast.type;

public enum BeastSendMsgType {
	
	/**
	 * NONE
	 */
	NONE(1),

	/**
	 * 被击败
	 */
	BEFORE_START(2),
	
	/**
	 * 时间到未击败
	 */
	START(4),

	/**
	 * 结束之前
	 */
	BEFORE_END(8),
	
	/**
	 * 时间到未击败
	 */
	ESCAPE_END(16),
	
	/**
	 * 时间到未击败
	 */
	BEAT_END(32);
	
	private int type;
	
	private BeastSendMsgType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static BeastSendMsgType getType(int type) {
		for (BeastSendMsgType endType : values()) {
			if (type == endType.getType()) {
				return endType;
			}
		}
		return null;
	}
}
