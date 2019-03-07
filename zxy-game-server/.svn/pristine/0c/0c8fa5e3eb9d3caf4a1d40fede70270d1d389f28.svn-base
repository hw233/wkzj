package com.jtang.gameserver.module.ally.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 与盟友切磋
 * @author pengzy
 *
 */
public class AllyFightRequest extends IoBufferSerializer {

	/**
	 * 盟友Id
	 */
	public long allyActorId;
	
	public AllyFightRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		allyActorId = readLong();
	}

}
