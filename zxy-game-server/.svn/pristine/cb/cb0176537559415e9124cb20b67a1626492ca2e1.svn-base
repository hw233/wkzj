package com.jtang.gameserver.module.gift.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 送礼请求
 * @author vinceruan
 *
 */
public class GiveGiftRequest extends IoBufferSerializer {
	/**
	 * 盟友角色id
	 */
	public long allyActorId;
	
	public GiveGiftRequest(byte[] data) {		
		super(data);
	}

	@Override
	public void read() {
		this.allyActorId = this.readLong();
	}

}
