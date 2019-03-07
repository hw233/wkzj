package com.jtang.gameserver.module.smelt.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class SmeltResponse extends IoBufferSerializer {
	
	/**
	 * 魂的数量
	 */
	public int soulNum;
	
	/**
	 * 尘的数量
	 */
	public int dustNum;
	
	public SmeltResponse(int soulNum,int dustNum){
		this.soulNum = soulNum;
		this.dustNum = dustNum;
	}
	
	@Override
	public void write() {
		writeInt(soulNum);
		writeInt(dustNum);
	}

}
