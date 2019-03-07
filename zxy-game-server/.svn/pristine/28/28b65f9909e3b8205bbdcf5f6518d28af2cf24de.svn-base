package com.jtang.gameserver.module.extapp.basin.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class BasinStateResponse extends IoBufferSerializer {

	/**
	 * 活动状态
	 * 0.关闭 1.开启
	 */
	public int state;
	
	public BasinStateResponse(boolean state){
		this.state = state == false ? 0 : 1;
	}
	
	@Override
	public void write() {
		writeByte((byte)state);
	}
}
