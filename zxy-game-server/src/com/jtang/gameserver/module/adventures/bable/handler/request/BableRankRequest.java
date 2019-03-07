package com.jtang.gameserver.module.adventures.bable.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class BableRankRequest extends IoBufferSerializer {

	/**
	 * 登天塔类型
	 */
	public byte[] bableType;
	
	public BableRankRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		bableType = readByteArray();
	}
	
}
