package com.jtang.gameserver.module.adventures.shop.shop.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class BlackShopRequest extends IoBufferSerializer {

	/**
	 * 兑换配置id
	 */
	public int exchangeId;
	
	public BlackShopRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		exchangeId = this.readInt();
	}
}
