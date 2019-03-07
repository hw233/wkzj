package com.jtang.gameserver.module.ladder.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class BuyFightNumResponse extends IoBufferSerializer {

	/**
	 * 下次购买需要的点券
	 */
	public int costTicket;
	
	public BuyFightNumResponse(int costTicket){
		this.costTicket = costTicket;
	}
	
	@Override
	public void write() {
		writeInt(costTicket);
	}
}
