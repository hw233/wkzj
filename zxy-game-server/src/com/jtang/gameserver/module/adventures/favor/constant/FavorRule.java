package com.jtang.gameserver.module.adventures.favor.constant;

import com.jtang.core.utility.TimeConstant;
import com.jtang.gameserver.dataconfig.service.GlobalService;

public class FavorRule {
	public static int REFRESH_TIME = 0;//0-23点
	
	public static long CONTAIN_TIME = 1;//持续时间（秒）
	
	public static int PERMANET_NEED_TOTAL_TICKET = 10;//永久使用福神眷顾需要要充值点券数
	
	static {
		REFRESH_TIME = GlobalService.getInt("FAVOR_REFRESH_TIME");
		CONTAIN_TIME = GlobalService.getInt("FAVOR_CONTAIN_TIME") * TimeConstant.ONE_HOUR_SECOND;
		PERMANET_NEED_TOTAL_TICKET = GlobalService.getInt("FAVOR_PERMANET_NEED_TOTAL_TICKET");
	}
}
