package com.jtang.gameserver.module.love.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class LoveMonsterRequest extends IoBufferSerializer {

	/**
	 * 难度id
	 */
	public int id;
	
	public LoveMonsterRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.id = readInt();
	}
}
