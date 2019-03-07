package com.jtang.gameserver.module.smelt.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class SmeltExchangeRequest extends IoBufferSerializer {

	/**
	 * 兑换的魂魄id
	 */
	public int heroId;
	
	/**
	 * 兑换的数量
	 */
	public int num;
	
	public SmeltExchangeRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		heroId = readInt();
		num = readInt();
	}
}
