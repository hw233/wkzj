package com.jtang.gameserver.module.herobook.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class HeroBookRewardRequest extends IoBufferSerializer {

	/**
	 * 奖励序号
	 */
	public int orderId;
	
	public HeroBookRewardRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.orderId = readInt();
	}

}
