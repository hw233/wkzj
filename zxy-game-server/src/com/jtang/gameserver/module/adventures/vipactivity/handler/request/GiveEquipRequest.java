package com.jtang.gameserver.module.adventures.vipactivity.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class GiveEquipRequest extends IoBufferSerializer {
	
	/**
	 * 接收人的id
	 */
	public long targetActor;

	public GiveEquipRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.targetActor  = readLong();
	}

}
