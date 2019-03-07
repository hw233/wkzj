package com.jtang.gameserver.module.hole.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class AllyFightNumResponse extends IoBufferSerializer {

	/**
	 * 通关盟友数量
	 */
	public int allyNum;
	
	
	public AllyFightNumResponse(int allyNum){
		this.allyNum = allyNum;
	}
	
	@Override
	public void write() {
		writeInt(allyNum);
	}

}
