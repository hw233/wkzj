package com.jtang.gameserver.module.ladder.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class LadderPushResponse extends IoBufferSerializer {

	/**
	 * 下次刷新对手扣除的金钱
	 */
	public int costGold;
	
	/**
	 * 下次补满次数扣除的点券
	 */
	public int costTicket;
	
	@Override
	public void write() {
		writeInt(costGold);
		writeInt(costTicket);
	}
}
