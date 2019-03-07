package com.jtang.gameserver.module.extapp.basin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class BasinRewardRequest extends IoBufferSerializer {

	/**
	 * 领取多少等级的奖励
	 */
	public int recharge;
	
	public BasinRewardRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.recharge = readInt();
	}
}
