package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 角色禁言请求
 * @author ligang
 */
public class ActorChatForbiddenRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	/**
	 * 禁言结束时间
	 */
	public int unforbiddenTime;
	
	public ActorChatForbiddenRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	protected void read() {
		this.actorId = readLong();
		this.unforbiddenTime = readInt();
	}
}
