package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 添加盟友
 * @author ludd
 *
 */
public class AddAllayRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 盟友id
	 */
	public long allyId;
	
	public AddAllayRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.allyId = readLong();
	}

}
