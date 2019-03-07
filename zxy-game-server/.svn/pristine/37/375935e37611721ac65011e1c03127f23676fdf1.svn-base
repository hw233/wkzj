package com.jtang.gameserver.module.battle.constant;

/**
 * 胜负的等级
 * @author vinceruan
 *
 */
public enum WinLevel {
	/**
	 * 不存在的类型
	 */
	NONE(-1),
	
	/**
	 * 大败
	 */
	BIG_FAIL(1),
	
	/**
	 * 失败
	 */
	FAIL(2),
	
	/**
	 * 惜败
	 */
	SMALL_FAIL(3),
	
	/**
	 * 险胜
	 */
	SMALL_WIN(4),
	
	/**
	 * 胜利
	 */
	WIN(5),
	
	/**
	 * 大胜
	 */
	BIG_WIN(6);
	
	private byte code;
	
	private WinLevel(int code) {
		this.code = (byte) code;
	}

	public byte getCode() {
		return code;
	}
	
	/**
	 * 是否胜利
	 * @return
	 */
	public boolean isWin() {
		return this == SMALL_WIN || this == WIN || this == BIG_WIN;
	}
	
	/**
	 * 是否失败
	 * @return
	 */
	public boolean isFail() {
		return isWin() == false;
	}
	
	public static WinLevel getByCode(int code) {
		for (WinLevel level : WinLevel.values()) {
			if (level.getCode() == code) {
				return level;
			}
		}
		return NONE;
	}
	
	public WinLevel getEnemyWinLevel() {
		switch (this) {
		case BIG_WIN:
			return BIG_FAIL;
			
		case WIN:
			return FAIL;
			
		case SMALL_WIN:
			return SMALL_FAIL;
			
		case SMALL_FAIL:
			return SMALL_WIN;
			
		case FAIL:
			return WIN;
			
		case BIG_FAIL:
			return BIG_WIN;

		default:
			return NONE;
		}
	}
}
