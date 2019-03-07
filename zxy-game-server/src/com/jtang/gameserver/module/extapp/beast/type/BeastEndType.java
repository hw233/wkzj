package com.jtang.gameserver.module.extapp.beast.type;


public enum BeastEndType {

	/**
	 * 被击败
	 */
	BEAT(1),
	
	/**
	 * 时间到未击败
	 */
	ESCAPE(2);
	
	
	private final int type;
	
	private BeastEndType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static BeastEndType getType(int type) {
		for (BeastEndType endType : values()) {
			if (type == endType.getType()) {
				return endType;
			}
		}
		return null;
	}
}
