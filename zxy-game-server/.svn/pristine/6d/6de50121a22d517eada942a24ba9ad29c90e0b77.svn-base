package com.jtang.gameserver.module.extapp.deitydesc.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class DeityDescendHitRequest extends IoBufferSerializer{

	/**
	 * 砸蛋次数
	 */
	public byte hitCount;

	public DeityDescendHitRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.hitCount = readByte();
	}
	
}
