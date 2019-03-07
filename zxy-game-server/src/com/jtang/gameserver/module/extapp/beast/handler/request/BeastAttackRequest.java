package com.jtang.gameserver.module.extapp.beast.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 攻击年兽
 * @author ligang
 *
 */
public class BeastAttackRequest extends IoBufferSerializer {

	/**
	 * 是否使用元宝
	 * 0 未使用 1 使用
	 */
	public byte useTicket;

	public BeastAttackRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.useTicket = readByte();
	}
	

}
