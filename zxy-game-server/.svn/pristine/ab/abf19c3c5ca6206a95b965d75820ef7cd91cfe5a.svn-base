package com.jtang.gameserver.module.gift.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

/**
 * 礼物模块常量配置
 * @author vinceruan
 *
 */
public class GiftRule {
	/**
	 * 达到指定收礼次数可领取大礼包
	 */
	public static int GIFT_ACCEPT_NUM_4_PACKAGE; // = 5;
	
	/**
	 * 每天最大送礼次数
	 */
	public static int GIFT_MAX_GIVE_NUM; // = 5; 
	
	/**
	 * 每天几点重置
	 */
	public static int GIFT_RESET_HOUR; // = 3;
	
	/**
	 * 每天接受礼物数量限制
	 */
	public static int ACCEPT_GIFT_CAP;
	
	static {
		GIFT_ACCEPT_NUM_4_PACKAGE = GlobalService.getInt("GIFT_ACCEPT_NUM_4_PACKAGE");
		GIFT_MAX_GIVE_NUM = GlobalService.getInt("GIFT_MAX_GIVE_NUM");
		GIFT_RESET_HOUR = GlobalService.getInt("GIFT_RESET_HOUR");
		ACCEPT_GIFT_CAP = GlobalService.getInt("ACCEPT_GIFT_CAP");
	}
}
