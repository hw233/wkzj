package com.jtang.gameserver.module.hole.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class HoleRewardRequest extends IoBufferSerializer {

	/**
	 * 自增id
	 */
	public long id;

	public HoleRewardRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.id = readLong();
	}

}
