package com.jtang.gameserver.module.msg.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class SendMsgRequest extends IoBufferSerializer {

	/**
	 * 接收消息者
	 */
	public long toActorId;
	/**
	 * 消息内容
	 */
	public String content;
	
	public SendMsgRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		toActorId = readLong();
		content = readString();
	}

}
