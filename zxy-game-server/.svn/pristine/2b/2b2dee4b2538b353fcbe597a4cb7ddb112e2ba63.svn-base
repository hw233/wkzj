package com.jtang.gameserver.module.adventures.vipactivity.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class GiveEquipResponse extends IoBufferSerializer {
	/**
	 * 赠送的装备id
	 */
	public int equipId;
	
	public GiveEquipResponse(int equipId){
		this.equipId = equipId;
	}

	@Override
	public void write() {
		writeInt(equipId);
	}
}
