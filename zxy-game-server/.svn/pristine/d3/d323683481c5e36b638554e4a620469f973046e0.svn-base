package com.jtang.gameserver.module.goods.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class StartComposeResponse extends IoBufferSerializer {

	/**
	 * 合成剩余时间
	 */
	public int time;
	
	public StartComposeResponse(int time){
		this.time = time;
	}
	
	@Override
	public void write() {
		writeInt(time);
	}
}
