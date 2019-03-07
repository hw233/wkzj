package com.jtang.gameserver.module.gift.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 推送我已经接受其礼物的盟友id
 * @author vinceruan
 *
 */
public class PushAcceptedGiftResponse extends IoBufferSerializer {
	public long allyActorId;

	@Override
	public void write() {
		this.writeLong(allyActorId);
	}
	
	public PushAcceptedGiftResponse(long allyActorId) {
		this.allyActorId = allyActorId;
	}

}
