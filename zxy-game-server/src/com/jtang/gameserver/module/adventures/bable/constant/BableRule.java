package com.jtang.gameserver.module.adventures.bable.constant;

import java.util.Calendar;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class BableRule {

	/**
	 * 每日进入次数 dayEnterNum:每日可进入次数(每天cleanTime重置)
	 */
	public static int BABLE_DAY_ENTER_NUM; // = 3;

	/**
	 * 清零时间 3点 cleanTime:清零时间(24小时制) 例：3代表三点 23代表二十三点
	 */
	public static int BABLE_CLEAN_TIME; // = 3; // cleanTime
	
	/**
	 * 排行榜显示数量
	 */
	public static int BABLE_RANK_NUM = 20;
	
	/**
	 * 登天塔重试次数
	 */
	public static int BABLE_REFIGHT_NUM = 2;
	
	
	
	static {
		BABLE_DAY_ENTER_NUM = GlobalService.getInt("BABLE_DAY_ENTER_NUM");
		BABLE_CLEAN_TIME = GlobalService.getInt("BABLE_CLEAN_TIME");		
		BABLE_RANK_NUM = GlobalService.getInt("BABLE_RANK_NUM");
		BABLE_REFIGHT_NUM = GlobalService.getInt("BABLE_REFIGHT_NUM");
	}
	
	/**
	 * 获取本次清理时间
	 * @return
	 */
	public static int getClearTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, BABLE_CLEAN_TIME);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Long time = c.getTime().getTime() / 1000;
		return time.intValue();
	}
	
	/**
	 * 获取上一次清理时间
	 * @return
	 */
	public static int getLastClearTime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		c.set(Calendar.HOUR_OF_DAY, BABLE_CLEAN_TIME);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Long time = c.getTime().getTime() / 1000;
		return time.intValue();
	}
}
