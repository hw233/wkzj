package com.jtang.gameserver.module.app.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class AppRule {
	
	/**
	 * 一元礼包购买等级限制
	 */
	public static int APP_LEVEL_LIMIT;
	
	/**
	 * 首冲双倍限制金额
	 */
	public static int APP_RECARGE_MONEY_LIMIT;
	
	/**
	 * 小v活动礼包购买等级限制
	 */
	public static int APP_LOW_VIP_LEVEL_LIMIT;
	
	/**
	 * 中V活动礼包参与等级限制
	 */
	public static int APP_MIDDLE_VIP_LEVEL_LIMIT;
	
	static{
		APP_LEVEL_LIMIT = GlobalService.getInt("APP_LEVEL_LIMIT");
		APP_RECARGE_MONEY_LIMIT = GlobalService.getInt("APP_RECARGE_MONEY_LIMIT");
		APP_LOW_VIP_LEVEL_LIMIT = GlobalService.getInt("APP_LOW_VIP_LEVEL_LIMIT");
		APP_MIDDLE_VIP_LEVEL_LIMIT = GlobalService.getInt("APP_MIDDLE_VIP_LEVEL_LIMIT");
	}

}
