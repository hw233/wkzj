package com.jtang.gameserver.module.adventures.vipactivity.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class ActorResetRule {
	/**
	 * 使用重生功能vip等级
	 */
	public static int RESET_USE_VIP_LEVEL = 1;
	/**
	 * 重生功能使用时封号时间
	 */
	public static int RESET_USE_TIME = 1;
	/**
	 * 重生key
	 */
	public static int RESET_ACOTR_KEY = 5000;
	static {
		RESET_USE_VIP_LEVEL = GlobalService.getInt("RESET_USE_VIP_LEVEL");
		RESET_USE_TIME = GlobalService.getInt("RESET_USE_TIME");
	}
}
