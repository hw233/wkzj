package com.jtang.gameserver.module.gift.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 收礼请求
 * @author vinceruan
 *
 */
public class AcceptGiftRequest extends IoBufferSerializer {
	/**
	 * 盟友的角色id
	 */
	public long allyActorId;

	public AcceptGiftRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.allyActorId = readLong();
	}
	
}
