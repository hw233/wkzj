package com.jtang.gameserver.module.hero.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class HeroRule {
	
	/**
	 * 单个玩家可以获得的最大仙人数
	 */
	public static int HERO_MAX_COUNT; // = 300;
	
	/**
	 * 单个玩家可以获得的最大仙人魂魄数
	 */
	public static int HERO_SOUL_MAX_COUNT; // = 300;
	
	/**
	 * 通过突破获取经验时:需要的魂魄数量表达式
	 * <pre>
	 * 	x1:星级
	 * </pre>
	 */
	public static String HERO_BREAKOUT_NEED_SOUL_EXPR; // = "x1*2";
	
	/**
	 * 通过突破获取经验时:产出的经验数量表达式
	 * <pre>
	 * 	 x1:星级
	 * 	 x2:英雄等级
	 * </pre>
	 */
	public static String HERO_BREAKOUT_OUTPUT_EXP_EXPR; // = "x1*x2";
	
	/**
	 * 当前仙人最大等级
	 * <pre>
	 * 	 x1:掌教等级
	 * </pre>
	 */
	public static String HERO_LEVEL_MAX_EXPR; // = "x1 * 3"
		
	static {
		HERO_MAX_COUNT = GlobalService.getInt("HERO_MAX_COUNT");
		HERO_SOUL_MAX_COUNT = GlobalService.getInt("HERO_SOUL_MAX_COUNT");
		HERO_BREAKOUT_NEED_SOUL_EXPR = GlobalService.get("HERO_BREAKOUT_NEED_SOUL_EXPR");
		HERO_BREAKOUT_OUTPUT_EXP_EXPR = GlobalService.get("HERO_BREAKOUT_OUTPUT_EXP_EXPR");
		HERO_LEVEL_MAX_EXPR = GlobalService.get("HERO_LEVEL_MAX_EXPR");
	}
}
