package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 踢出玩家请求
 * @author ludd
 *
 */
public class KickPlayerRequest extends IoBufferSerializer {

	public long actorId;
	
	public KickPlayerRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
	}

}
