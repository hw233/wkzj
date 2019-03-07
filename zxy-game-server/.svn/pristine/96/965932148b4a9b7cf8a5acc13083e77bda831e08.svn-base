package com.jtang.gameserver.module.user.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 创建角色返回结果
 * @author 0x737263
 *
 */
public class CreateActorResponse extends IoBufferSerializer {

	/**
	 * 角色Id
	 */
	public long actorId;
	
	@Override
	public void write() {
		writeLong(this.actorId);
	}

}
