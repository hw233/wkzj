package com.jtang.gameserver.module.buffer.type;
/**
 * 定身状态
 * @author ludd
 *
 */
public enum ImmobilezeState {
	/**
	 * 冰冻
	 */
	ICE(1, 2, "冰冻"),
	/**
	 * 缠绕
	 */
	BIND(2, 1, "缠绕"),
	/**
	 * 眩晕
	 */
	VERTIGO(3, 1, "眩晕"),
	/**
	 * 封印
	 */
	SEAL(4,1,"封印"),
	/**
	 * 捕捉
	 */
	SEIZE(5, 1,"捕捉"),
	/**
	 * 麻痹
	 */
	BENUMB(6, 1, "麻痹"),
	/**
	 * 魅惑
	 */
	CONFUSE(7, 1, "魅惑");
	/**
	 * 属性状态
	 */
	private final byte state;
	/**
	 * 权重
	 */
	private final int weight;
	private final String desc;
	private ImmobilezeState(int state, int weight, String desc){
		this.state = (byte) state;
		this.desc = desc;
		this.weight = weight;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public byte getState() {
		return state;
	}
	
	public static ImmobilezeState getByState(byte state) {
		for (ImmobilezeState immobilezeState : values()) {
			if (immobilezeState.getState() == state){
				return immobilezeState;
			}
		}
		return null;
	}
	
	public int getWeight() {
		return weight;
	}
}
