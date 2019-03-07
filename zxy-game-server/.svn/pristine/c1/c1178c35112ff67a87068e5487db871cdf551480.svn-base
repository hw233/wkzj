package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class ChangeBlockRequest extends IoBufferSerializer {

	/**
	 * 是否重置
	 * 1：是
	 * 0：否
	 */
	public int flag;
	
	public ChangeBlockRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.flag = readInt();
	}

}
