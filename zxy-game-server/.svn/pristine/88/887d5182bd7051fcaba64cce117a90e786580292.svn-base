package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 添加故事信息
 * 
 * @author ludd
 * 
 */
public class AddStoryRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;

	/**
	 * 星级
	 */
	public byte star;

	public AddStoryRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.actorId = readLong();
		this.star = readByte();
	}

}
