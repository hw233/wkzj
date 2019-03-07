package com.jtang.gameserver.module.extapp.invite.type;


public enum ReceiveStatusType {

	/**
	 * 未达到领取条件
	 */
	DID_NOT_RECEIVE(1),
	/**
	 * 可领取
	 */
	CAN_RECEIVE(2),
	/**
	 *已领取
	 */
	HAVE_RECEIVED(3);
	
	private final int type;
	
	private ReceiveStatusType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static ReceiveStatusType getByType(int type) {
		for (ReceiveStatusType statusType : values()) {
			if (type == statusType.getType()) {
				return statusType;
			}
		}
		return null;
	}
}
