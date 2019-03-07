package com.jtang.gameserver.module.adventures.bable.type;
/**
 * 登天塔状态
 * @author ludd
 *
 */
public enum BableState {
	/**
	 * 登天塔外
	 */
	OUT_BABLE(0),
	/**
	 * 登塔中
	 */
	INT_BABLE(1),
	/**
	 * 结束
	 */
	BABLE_END(2);
	
	private final byte state;
	private BableState(int state) {
		this.state = (byte)state;
	}
	
	public byte getState() {
		return state;
	}
	
	public static BableState getByState(int state){
		for (BableState bableState : values()) {
			if (bableState.getState() == state){
				return bableState;
			}
		}
		return OUT_BABLE;
	}
}
