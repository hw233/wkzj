package com.jtang.gameserver.module.extapp.welkin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class WelkinRequest extends IoBufferSerializer {

	/**
	 * 抽奖次数
	 * @param bytes
	 */
	public int count;
	
	
	public WelkinRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	protected void read() {
		this.count = readInt();
	}
	
}
