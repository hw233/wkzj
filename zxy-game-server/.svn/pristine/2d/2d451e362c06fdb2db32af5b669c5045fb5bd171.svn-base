package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 删除故事信息
 * @author ludd
 *
 */
public class DeleteStoryRequest extends IoBufferSerializer {
	/**
	 * 角色id
	 */
	public long actorId;
	
	
	public DeleteStoryRequest(byte[] bytes) {
		super(bytes);
	}
	@Override
	public void read() {
		this.actorId = readLong();
	}

}
