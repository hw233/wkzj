package com.jtang.gameserver.module.delve.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class LastDelveRequest extends IoBufferSerializer {
	
	/**
	 * 仙人id
	 */
	public int heroId;
	
	public LastDelveRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		heroId = readInt();
	}

}
