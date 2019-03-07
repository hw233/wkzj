package com.jtang.gameserver.module.power.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class PowerFightRequest extends IoBufferSerializer {
	/**
	 * 被挑战的角色Id
	 */
	public long targetActorId;
	
	public PowerFightRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		targetActorId = readLong();
	}
}
