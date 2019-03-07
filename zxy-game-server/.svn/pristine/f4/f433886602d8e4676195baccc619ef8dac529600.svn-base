package com.jtang.gameserver.module.treasure.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class TreasureExchangeResponse extends IoBufferSerializer {

	/**
	 * 兑换物品剩余数量
	 */
	public int exchangeNum;

	public TreasureExchangeResponse(int exchangeNum) {
		this.exchangeNum = exchangeNum;
	}

	@Override
	public void write() {
		writeInt(exchangeNum);	
	}

}
