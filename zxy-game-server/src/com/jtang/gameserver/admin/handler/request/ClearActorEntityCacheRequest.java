package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class ClearActorEntityCacheRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	public ClearActorEntityCacheRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.actorId = readLong();
	}

}
