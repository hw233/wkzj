package com.jtang.gameserver.module.praise.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class PraiseDataResponse extends IoBufferSerializer {

	/**
	 * 是否激活（0：未激活，1：激活）
	 */
	private byte isActive;
	
	/**
	 * 是否领取了激活奖励（0：未领，1：已领）
	 */
	private byte activeRewardGet;
	
	/**
	 *  是否领取了好评奖励（0：未领，1：已领）
	 */
	private byte commentRewardGet;
	
	
	public PraiseDataResponse(byte isActive, byte activeRewardGet, byte commentRewardGet) {
		super();
		this.isActive = isActive;
		this.activeRewardGet = activeRewardGet;
		this.commentRewardGet = commentRewardGet;
	}


	@Override
	public void write() {
		this.writeByte(isActive);
		this.writeByte(activeRewardGet);
		this.writeByte(commentRewardGet);
	}

}
