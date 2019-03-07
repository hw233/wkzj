package com.jtang.gameserver.module.smelt.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class SmeltRule {
	
	/**
	 * 魂的物品id
	 */
	public static int SOUL_GOODS_ID;
	
	/**
	 * 尘的物品id
	 */
	public static int DUST_GOODS_ID;
	
	static {
		SOUL_GOODS_ID = GlobalService.getInt("SOUL_GOODS_ID");
		DUST_GOODS_ID = GlobalService.getInt("DUST_GOODS_ID");
	}
}
