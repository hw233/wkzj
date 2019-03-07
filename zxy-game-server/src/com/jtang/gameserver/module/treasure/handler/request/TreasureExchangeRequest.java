package com.jtang.gameserver.module.treasure.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class TreasureExchangeRequest extends IoBufferSerializer {
	
	/**
	 * 兑换id
	 */
	public int exchangeId;
	
	/**
	 * 兑换数量
	 */
	public int num;

	public TreasureExchangeRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		exchangeId = readInt();
		num = readInt();
	}

}
