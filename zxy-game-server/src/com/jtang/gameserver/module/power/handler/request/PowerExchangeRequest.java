package com.jtang.gameserver.module.power.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class PowerExchangeRequest extends IoBufferSerializer {

	/**
	 * 兑换id
	 */
	public int exchangeId;
	
	/**
	 * 兑换数量
	 */
	public int exchangeNum;
	
	public PowerExchangeRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.exchangeId = readInt();
		this.exchangeNum = readInt();
	}
}
