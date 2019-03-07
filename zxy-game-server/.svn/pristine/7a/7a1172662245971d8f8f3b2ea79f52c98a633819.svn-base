package com.jtang.gameserver.module.adventures.vipactivity.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class ResetHeroRequest extends IoBufferSerializer {

	/**
	 * 仙人id
	 */
	public int heroId;
	
	public ResetHeroRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.heroId = readInt();
	}
}
