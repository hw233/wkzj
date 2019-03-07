package com.jtang.gameserver.module.icon.hander.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class IconRequest extends IoBufferSerializer {
	
	/**
	 * 设置的头像/边框
	 */
	public int iocn;
	
	public IconRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.iocn = readInt();
	}

}
