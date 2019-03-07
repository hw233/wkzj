package com.jtang.gameserver.module.ladder.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class LadderFightRequest extends IoBufferSerializer {

	
	/**
	 * 对手actorId
	 */
	public long targetActorId;
	
	public LadderFightRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.targetActorId = readLong();
	}
}
