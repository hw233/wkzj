package com.jtang.gameserver.module.battle.type;

/**
 * 精灵的种族类型
 * @author vinceruan
 *
 */
public enum SpriteType {
	HERO(1),
	MONSTER(2);
	
	private int code;
	
	private SpriteType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
