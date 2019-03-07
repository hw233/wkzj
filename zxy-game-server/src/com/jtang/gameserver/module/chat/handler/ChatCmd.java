package com.jtang.gameserver.module.chat.handler;


public interface ChatCmd {

	/**
	 * 世界聊天
	 * 
	 * <pre>
	 * 请求：{@code ChatRequest}
	 * </pre>
	 * 
	 */
	byte WORLD_CHAT = 1;
	
	/**
	 * 推送消息
	 * 回复：{@code ChatResponse}
	 */
	byte BROADCAST_CHAT = 2;
	
	/**
	 * 推送旧聊天记录
	 * 回复:{@code HistoryChatResponse}
	 * */
	byte HISTORY_CHAT = 3;
}
