package com.jtang.gameserver.module.chat.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 聊天响应,该响应将广播给所有在线角色
 * 
 * @author pengzy
 * 
 */
public class HistoryChatResponse extends IoBufferSerializer {

	/**
	 * 历史聊天
	 */
	List<ChatResponse> msgList;

	public HistoryChatResponse(List<ChatResponse> list) {
		this.msgList = list;
	}

	@Override
	public void write() {
		writeShort((short) msgList.size());
		for (ChatResponse msg : msgList) {
			this.writeBytes(msg.getBytes());
		}
	}

}
