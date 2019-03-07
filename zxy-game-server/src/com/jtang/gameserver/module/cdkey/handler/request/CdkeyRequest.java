package com.jtang.gameserver.module.cdkey.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class CdkeyRequest extends IoBufferSerializer {

	/**
	 * cdkey
	 */
	public String cdkey;

	/**
	 * 玩家id
	 */
	public long actorId;

	public CdkeyRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		cdkey = readString();
		actorId = readLong();
	}

}
