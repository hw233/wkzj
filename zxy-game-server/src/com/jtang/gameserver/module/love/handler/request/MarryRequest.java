package com.jtang.gameserver.module.love.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class MarryRequest extends IoBufferSerializer {
	/**
	 * 目标角色id
	 */
	public long targetActorId;

	public MarryRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	protected void read() {
		this.targetActorId = readLong();
	}
	
	
}
