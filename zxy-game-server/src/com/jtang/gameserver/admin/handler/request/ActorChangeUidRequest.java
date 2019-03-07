package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 修改角色帐号归属请求
 * @author 0x737263
 *
 */
public class ActorChangeUidRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 新的平台id
	 */
	public int newPlatformId;
	
	/**
	 * 新的uid
	 */
	public String newUid;
	
	/**
	 * 新的渠道id
	 */
	public int newChannelId;

	/**
	 * @param bytes
	 */
	public ActorChangeUidRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.actorId = readLong();
		this.newPlatformId = readInt();
		this.newUid = readString();
		this.newChannelId = readInt();
	}

}
