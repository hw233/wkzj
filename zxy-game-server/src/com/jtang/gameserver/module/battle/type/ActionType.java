package com.jtang.gameserver.module.battle.type;
/**
 * 效果类型
 * @author ludd
 *
 */
public enum ActionType {

	/**
	 * 技能效果
	 */
	SKILL_ACTION((byte)1),
	/**
	 *  buffer效果
	 */
	BUFFER_ACTION((byte)2),
	/**
	 * 移动效果
	 */
	MOVE_ACTION((byte)3),
	/**
	 * 掉落效果
	 */
	DROPGOODS_ACTION((byte)4),
	/**
	 * 逐一播放组合效果
	 */
	SEQUENCE_ACTION((byte)5),
	/**
	 * 同时播放组合效果
	 */
	SPAWN_ACTION((byte)6),
	/**
	 * 掉落属性效果
	 */
	DROPACTORPROPERTY_ACTION((byte)7),
	/**
	 * 平移效果
	 */
	POSITION_ACTION((byte)8), 
	/**
	 * 免疫效果
	 */
	IMMUNITY_ACTION((byte)9),
	
	/**
	 * 闪避效果
	 */
	DODGE_ACTION((byte)10), 
	
	/**
	 * 瞬移效果
	 */
	TELEPORT_ACTION((byte)11),
	/**
	 * 击退
	 */
	REPULSE_ACTION ((byte)12),
	/**
	 * 复活
	 */
	REVIVE_ACTION  ((byte)13),
	DEAD_ACTION  ((byte)14),
	DISAPPER_ACTION  ((byte)15);
	
	
	private final byte type;
	private ActionType(byte type) {
		this.type = type;
	}
	public byte getType() {
		return type;
	}
	
	
}
