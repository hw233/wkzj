package com.jtang.gameserver.module.chat.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class ChatRule {

	/**
	 * 天财地宝信息限制可见等级
	 */
	public static int TREASURE_LIMIT_LEVEL;
	/**
	 * 聊天消息长度限制
	 */
	public static int MSG_LENGTH_LIMIT; // = 60;
	/**
	 * 世界聊天消耗的道具数量计算公式
	 */
	public static String CHAT_CONSUME_GOLDS_EXPR;
	
	
	static {
		MSG_LENGTH_LIMIT = GlobalService.getInt("MSG_LENGTH_LIMIT");
		CHAT_CONSUME_GOLDS_EXPR = GlobalService.get("CHAT_CONSUME_GOLDS_EXPR");
		TREASURE_LIMIT_LEVEL = GlobalService.getInt("TREASURE_LIMIT_LEVEL");
	}	
}
