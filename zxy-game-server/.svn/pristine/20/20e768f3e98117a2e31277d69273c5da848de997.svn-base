package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 解除角色禁言请求
 * @author ligang
 */
public class ActorChatUnforbiddenRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	public ActorChatUnforbiddenRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	protected void read() {
		this.actorId = readLong();
	}
}
