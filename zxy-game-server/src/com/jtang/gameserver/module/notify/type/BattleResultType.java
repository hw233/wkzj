package com.jtang.gameserver.module.notify.type;

public enum BattleResultType {
	/**
	 * 胜利
	 */
	NONE(0),
	
	/**
	 * 胜利
	 */
	WIN(1),
	
	/**
	 * 失败
	 */
	FAIL(2);
	
	private byte code;
	
	private BattleResultType(int code) {
		this.code = (byte) code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static BattleResultType getByCode(int code) {
		return code == WIN.getCode() ? WIN : FAIL;
	}
	
	public static BattleResultType getType(boolean isWin) {
		return isWin ? BattleResultType.WIN : BattleResultType.FAIL;
	}
	
	
	/**
	 * 获取相反的结果
	 * @param isWin
	 * @return
	 */
	public static BattleResultType getReverseResult(boolean isWin){
		if(isWin) {
			return FAIL;
		}
		return WIN;
	}
}
