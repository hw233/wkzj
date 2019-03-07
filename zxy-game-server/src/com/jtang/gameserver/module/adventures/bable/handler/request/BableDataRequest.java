package com.jtang.gameserver.module.adventures.bable.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class BableDataRequest extends IoBufferSerializer {

	
	/**
	 * 登天塔类型
	 */
	public byte bableType;
	
	public BableDataRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		bableType = readByte(); 
	};
}
