package com.jtang.gameserver.module.notice.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class NoticeRule {
	
	/**
	 * 每日可广播次数
	 */
	public static int NOTICE_SEND_NUM_LIMIT = 100;
	
	static {
		NOTICE_SEND_NUM_LIMIT = GlobalService.getInt("NOTICE_SEND_NUM_LIMIT");
	}
}
