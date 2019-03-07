package com.jtang.gameserver.module.ladder.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class FightVideoRequest extends IoBufferSerializer {

	/**
	 * 录像id
	 */
	public long fightVideoId;
	
	public FightVideoRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.fightVideoId = readLong();
	}
}
