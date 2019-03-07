package com.jtang.gameserver.module.msg.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class MsgRule {

	/**
	 * 最大消息条数限制
	 */
	public static int MSG_MAX_NUM_LIMIT;
	/**
	 * 消息发送时间间隔限制：单位为妙
	 */
	public static int MSG_INTERVAL_TIME_LIMIT;

	static {
		MSG_MAX_NUM_LIMIT = GlobalService.getInt("MSG_MAX_NUM_LIMIT");
		MSG_INTERVAL_TIME_LIMIT = GlobalService.getInt("MSG_INTERVAL_TIME_LIMIT");
	}
}
