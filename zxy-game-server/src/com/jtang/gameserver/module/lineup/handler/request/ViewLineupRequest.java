package com.jtang.gameserver.module.lineup.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 查看阵型
 * @author pengzy
 *
 */
public class ViewLineupRequest extends IoBufferSerializer {
	
	/**
	 * 目标角色id
	 */
	public long actorId;
	
	public ViewLineupRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		actorId = readLong();
	}

}
