package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 添加声望请求
 * @author ludd
 *
 */
public class AddReputationRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	/**
	 * 声望值
	 */
	public int reputationValue;
	public AddReputationRequest(byte[] bytes) {
		super(bytes);
	}
	@Override
	public void read() {
		this.actorId = readLong();
		this.reputationValue = readInt();
	}

}
