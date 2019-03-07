package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class ActorEntityRequest extends IoBufferSerializer {

	/**
	 * 角色表名
	 */
	public String tableName;
	/**
	 * 角色id
	 */
	public long actorId;
	
	public ActorEntityRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.tableName = readString();
		this.actorId = readLong();
	}

}
