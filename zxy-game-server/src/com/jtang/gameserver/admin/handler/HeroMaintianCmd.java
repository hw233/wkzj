package com.jtang.gameserver.admin.handler;


public interface HeroMaintianCmd {
	/**
	 * 删除仙人
	 * 请求：{@code DeleteHeroRequest}
	 * 响应：{@code Response}
	 */
	byte DEL_HERO = 1;
	
	/**
	 * 增加仙人经验
	 * 请求：{@code AddHeroExpRequest}
	 * 响应:{@code Response}
	 */
	byte ADD_HERO_EXP = 2;
	
	/**
	 * 添加仙人魂魄
	 * 请求:{@code AddHeroSoulRequest}
	 * 响应：{@code Response}
	 */
	byte ADD_HERO_SOUL = 3;
	
	/**
	 * 删除仙人魂魄
	 * 请求:{@code DeleteHeroSoulRequest}
	 * 响应：{@code Response}
	 */
	byte DEL_HERO_SOUL = 4;
	
	/**
	 * 添加所有仙人魂魄
	 * 请求:{@code AddAllHeroSoulRequest}
	 * 响应：{@code Response}
	 */
	byte ADD_ALL_HERO_SOUL = 5;
}
