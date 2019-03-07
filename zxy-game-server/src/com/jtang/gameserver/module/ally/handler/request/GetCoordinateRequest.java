package com.jtang.gameserver.module.ally.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 在添加盟友之前请求其位置信息
 * @author pengzy
 *
 */
public class GetCoordinateRequest extends IoBufferSerializer {
	/**
	 * 将被加为盟友的角色Id
	 * 
	 */
	public long actorId;

	public GetCoordinateRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		actorId = readLong();
	}

}
