package com.jtang.gameserver.module.adventures.vipactivity.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class ResetEquipRequest extends IoBufferSerializer {

	/**
	 * 装备uuid
	 */
	public long uuid;
	
	public ResetEquipRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.uuid = readLong();
	}
}
