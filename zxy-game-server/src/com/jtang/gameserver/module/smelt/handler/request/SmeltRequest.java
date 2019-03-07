package com.jtang.gameserver.module.smelt.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class SmeltRequest extends IoBufferSerializer {

	/**
	 * 仙人/魂魄id
	 */
	public int heroId;
	
	/**
	 * 数量
	 * (仙人传0,魂魄传数量)
	 */
	public int num;
	
	public SmeltRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.heroId = readInt();
		this.num = readInt();
	}
}
