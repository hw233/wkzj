package com.jtang.gameserver.module.buffer.type;
/**
 * buffer效果枚举
 * @author ludd
 *
 */
public enum BufferEffectType {
	/**
	 * 1.减少攻击
	 */
	DECREASE_ATTACK(1),
	
	/**
	 * 2.减少防御
	 */
	DECREASE_DEFFENSE(2),
	
	/**
	 * 3.损失生命
	 */
	DECREASE_HP(3),
	
	/**
	 * 4.有概率当前回合无法行动。
	 */
	NOT_MOVE(4);
	private final int type;
	private BufferEffectType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static BufferEffectType valueOf(int type) {
		for (BufferEffectType bufferEffectType : values()) {
			if (bufferEffectType.getType() == type) {
				return bufferEffectType;
			}
		}
		return null;
	}
}
