package com.jtang.gameserver.module.icon.hander.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class SexRequest extends IoBufferSerializer {
	
	/**
	 * 设置性别
	 */
	public byte sex;
	
	public SexRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.sex = readByte();
	}

}
