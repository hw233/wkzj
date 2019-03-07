package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 删除盟友
 * @author ludd
 *
 */
public class DeleteAllayRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 盟友id
	 */
	public long allyId;
	
	public DeleteAllayRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.allyId = readLong();
	}

}
