package com.jtang.gameserver.module.demon.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class DemonRule {
	/**
	 * 集众降魔战斗地图id
	 */
	public static int DEMON_MAP_ID;
	static {

		DEMON_MAP_ID = GlobalService.getInt("DEMON_MAP_ID");
	}
}
