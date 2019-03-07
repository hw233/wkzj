package com.jtang.gameserver.module.hero.type;

/**
 * 仙人扣除类型
 * 
 * @author 0x737263
 * 
 */
public enum HeroDecreaseType {

	/**
	 * 吸灵消耗
	 */
	VAMPIIR(1),

	/**
	 * 仙人合成
	 */
	COMPOSE(2),

	/**
	 * 管理平台扣除
	 */
	ADMIN(3),
	
	/**
	 * 仙人重置
	 */
	HERO_RESET(4), 
	
	/**
	 * 轮回熔炉
	 */
	SMELT(5);

	private int id;

	private HeroDecreaseType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
