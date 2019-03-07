package com.jtang.gameserver.module.ally.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 添加盟友请求
 * 
 * @author pengzy
 * 
 */
public class AllyAddRequest extends IoBufferSerializer {

	/**
	 * 被加盟的角色Id
	 * 
	 */
	public long allyActorId;

	public AllyAddRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		allyActorId = readLong();
	}

}
