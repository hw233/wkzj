package com.jtang.gameserver.module.love.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class AcceptMarryRequest extends IoBufferSerializer {
	/**
	 * 同意 ：1
	 * 不同意：0
	 */
	public byte accept;
	
	/**
	 * 目标角色id
	 */
	public long targetActorId;

	public AcceptMarryRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	protected void read() {
		this.accept = readByte();
		this.targetActorId = readLong();
	}
	
	
}
