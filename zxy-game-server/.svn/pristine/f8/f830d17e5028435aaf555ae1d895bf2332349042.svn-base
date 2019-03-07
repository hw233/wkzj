package com.jtang.gameserver.module.love.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class LoveFightRequest extends IoBufferSerializer {

	/**
	 * 被挑战的角色id
	 */
	public long targetActorId;
	
	public LoveFightRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		targetActorId = readLong();
	}
}
