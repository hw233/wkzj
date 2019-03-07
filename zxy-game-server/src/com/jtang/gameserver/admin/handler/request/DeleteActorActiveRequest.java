package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class DeleteActorActiveRequest extends IoBufferSerializer {
	
	public long actorId;
	
	public long appId;

	
	public DeleteActorActiveRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.appId = readLong();
	}

}
