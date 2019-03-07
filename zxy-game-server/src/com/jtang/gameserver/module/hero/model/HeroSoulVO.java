package com.jtang.gameserver.module.hero.model;

public class HeroSoulVO {
	/**
	 * 仙人的配置ID
	 */
	public int heroId;
	
	/**
	 * 仙人的魂魄数量
	 */
	public int count;
	
	public static HeroSoulVO valueOf(int heroId, int count) {
		HeroSoulVO vo = new HeroSoulVO();
		vo.heroId = heroId;
		vo.count = count;
		return vo;
	}
}
