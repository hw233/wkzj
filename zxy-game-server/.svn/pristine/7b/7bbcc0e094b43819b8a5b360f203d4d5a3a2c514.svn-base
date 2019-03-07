package com.jtang.gameserver.module.demon.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class DemonScoreResponse extends IoBufferSerializer {

	/**
	 * 当前降魔积分
	 */
	private long demonScore;
	
	public DemonScoreResponse(long demonScore) {
		this.demonScore = demonScore;
	}
	@Override
	public void write() {
		this.writeLong(demonScore);
	}

}
