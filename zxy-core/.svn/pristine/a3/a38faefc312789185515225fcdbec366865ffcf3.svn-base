package com.jiatang.common.crossbattle.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class ReadFlagW2G extends IoBufferSerializer {
	/**
	 * 0:未读
	 * 1：已读
	 */
	public byte flag;

	public ReadFlagW2G(byte[] bytes) {
		setReadBuffer(bytes);
	}
	
	@Override
	protected void read() {
		this.flag = readByte();
	}
	
	@Override
	public void write() {
		this.writeByte(this.flag);
	}
	
}
