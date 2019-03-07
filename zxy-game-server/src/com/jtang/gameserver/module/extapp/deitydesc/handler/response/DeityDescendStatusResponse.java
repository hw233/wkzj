package com.jtang.gameserver.module.extapp.deitydesc.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class DeityDescendStatusResponse extends IoBufferSerializer{

	/**
	 * 状态 0关闭 1 开启
	 */
	public byte status;
	
	public DeityDescendStatusResponse(byte status) {
		this.status = status;
	}
	
	@Override
	public void write() {
		writeByte(this.status);
	}
}
