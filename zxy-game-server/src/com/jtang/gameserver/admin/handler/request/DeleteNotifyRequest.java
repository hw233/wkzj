package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class DeleteNotifyRequest extends IoBufferSerializer {

	/**
	 * actorId
	 */
	public long actorId;
	
	/**
	 * notifyId
	 */
	public long notifyId;
	
	
	public DeleteNotifyRequest(byte[] bytes){
		super(bytes);
	}

	@Override
	public void read() {
		actorId = readLong();
		notifyId = readLong();
	}

}
