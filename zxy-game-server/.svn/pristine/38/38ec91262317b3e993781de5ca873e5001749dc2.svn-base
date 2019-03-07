package com.jtang.gameserver.module.demon.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class AttackBossRequest extends IoBufferSerializer {

	/**
	 * 是否使用点券 0：不使用， 1：使用
	 */
	public byte useTicket;
	public AttackBossRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.useTicket = readByte();
	}
	

}
