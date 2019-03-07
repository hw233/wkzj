package com.jtang.gameserver.module.chat.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 世界聊天请求:包含聊天内容
 * 
 * @author pengzy
 * 
 */
public class ChatRequest extends IoBufferSerializer {
	/**
	 * 世界聊天消息
	 */
	public String msg;

	public ChatRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		msg = readString();
	}
}
