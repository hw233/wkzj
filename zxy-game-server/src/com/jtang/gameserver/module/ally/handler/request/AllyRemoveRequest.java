package com.jtang.gameserver.module.ally.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 删除盟友
 * @author pengzy
 *
 */
public class AllyRemoveRequest extends IoBufferSerializer {

	public long actorId;
	public AllyRemoveRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		actorId = readLong();
	}

}
