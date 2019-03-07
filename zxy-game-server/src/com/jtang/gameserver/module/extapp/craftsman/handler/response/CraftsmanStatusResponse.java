package com.jtang.gameserver.module.extapp.craftsman.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class CraftsmanStatusResponse extends IoBufferSerializer {

	public int time;
	
	public int buildNum;
	
	public CraftsmanStatusResponse(int time, int buildNum){
		this.time = time;
		this.buildNum = buildNum;
	}
	
	@Override
	public void write() {
		writeInt(this.time);
		writeInt(this.buildNum);
	}
	
}
