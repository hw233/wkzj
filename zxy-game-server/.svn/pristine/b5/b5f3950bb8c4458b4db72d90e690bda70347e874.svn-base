package com.jtang.gameserver.module.love.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class LoveGiftResponse extends IoBufferSerializer {
	/**
	 * 礼物状态
	 * 0：无，1：有，2：已收
	 */
	private byte hasGift;
	
	/**
	 * 0：没有，1：已送
	 */
	private byte hasGive;

	public LoveGiftResponse(byte hasGift, byte hasGive) {
		super();
		this.hasGift = hasGift;
		this.hasGive = hasGive;
	}
	
	@Override
	public void write() {
		this.writeByte(this.hasGift);
		this.writeByte(this.hasGive);
	}
}
