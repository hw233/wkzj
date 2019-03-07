package com.jtang.gameserver.module.hole.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class HoleRequest extends IoBufferSerializer {

	/**
	 * 洞府自增id
	 */
	public long id;

	public HoleRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.id = readLong();
	}

}
