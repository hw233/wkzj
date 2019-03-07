package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class DeleteMsgRequest extends IoBufferSerializer {
	
	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 消息id
	 */
	public long msgId;

	
	public DeleteMsgRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		actorId = readLong();
		msgId = readLong();
	}

}
