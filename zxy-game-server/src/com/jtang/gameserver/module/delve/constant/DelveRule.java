package com.jtang.gameserver.module.delve.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class DelveRule {
	/**
	 * 可使用潜修石最小星级
	 */
	public static int DELVE_USE_GOODS_MIN_STAR;
	/**
	 * 可使用潜修石最小等级
	 */
	public static int DELVE_USE_GOODS_MIN_LEVEL;
	
	/**
	 * 一键潜修失败多少次进行保底一次
	 */
	public static int ONE_KEY_DELVE_FAIL;
	
	static {

		DELVE_USE_GOODS_MIN_STAR = GlobalService.getInt("DELVE_USE_GOODS_MIN_STAR");
		DELVE_USE_GOODS_MIN_LEVEL = GlobalService.getInt("DELVE_USE_GOODS_MIN_LEVEL");
		ONE_KEY_DELVE_FAIL = GlobalService.getInt("ONE_KEY_DELVE_FAIL");
	}
}
