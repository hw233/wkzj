package com.jtang.gameserver.module.icon.hander.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class IconVORequest extends IoBufferSerializer {

	/**
	 * 获取头像边框的角色id
	 */
	public long actorId;

	public IconVORequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
	}
}
