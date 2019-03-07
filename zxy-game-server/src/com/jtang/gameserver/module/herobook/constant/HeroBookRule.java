package com.jtang.gameserver.module.herobook.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class HeroBookRule {
	/**
	 * 开放掌教等级
	 */
	public static int OPEN_ACTOR_LEVEL = 1;
	
	static {
		OPEN_ACTOR_LEVEL = GlobalService.getInt("HERO_BOOK_OPEN_ACTOR_LEVEL");
	}
}
