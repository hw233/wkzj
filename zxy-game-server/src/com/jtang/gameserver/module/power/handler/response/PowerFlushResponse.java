package com.jtang.gameserver.module.power.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class PowerFlushResponse extends IoBufferSerializer {

	/**
	 * 已经刷新的次数
	 */
	public int flushNum;
	
	public PowerFlushResponse(int flushNum){
		this.flushNum = flushNum;
	}
	
	@Override
	public void write() {
		writeInt(flushNum);
	}
}
