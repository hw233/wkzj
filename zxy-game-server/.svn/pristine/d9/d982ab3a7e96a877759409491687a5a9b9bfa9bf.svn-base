package com.jtang.gameserver.module.hole.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class HoleFightRequest extends IoBufferSerializer {

	/**
	 * 洞府自增id
	 */
	public long id;
	
	/**
	 * 战场id
	 */
	public int battleId;
	
	public HoleFightRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.id = readLong();
		this.battleId = readInt();
	}

}
