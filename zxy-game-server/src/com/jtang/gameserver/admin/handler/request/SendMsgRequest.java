package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class SendMsgRequest extends IoBufferSerializer {

	/**
	 * 发送人id
	 */
	public long actorId;

	/**
	 * 接收人id
	 */
	public long toActorId;

	/**
	 * 消息内容
	 */
	public String content;

	public SendMsgRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.actorId = readLong();
		this.toActorId = readLong();
		this.content = readString();
	}

}
