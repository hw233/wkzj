package com.jtang.gameserver.module.snatch.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class SnatchNumResponse extends IoBufferSerializer {

	/**
	 * 购买抢夺次数需要的点券
	 */
	public int costTicket;
	
	/**
	 * 已购买次数
	 */
	public int buySnatchNum;
	
	/**
	 * 剩余抢夺次数
	 */
	public int snatchNum;
	
	public SnatchNumResponse(int costTicket,int buySnatchNum,int snatchNum){
		this.costTicket = costTicket;
		this.buySnatchNum = buySnatchNum;
		this.snatchNum = snatchNum;
	}
	
	@Override
	public void write() {
		writeInt(costTicket);
		writeInt(buySnatchNum);
		writeInt(snatchNum);
	}
}
