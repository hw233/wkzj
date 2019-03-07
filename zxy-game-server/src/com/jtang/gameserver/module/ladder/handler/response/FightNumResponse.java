package com.jtang.gameserver.module.ladder.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class FightNumResponse extends IoBufferSerializer {

	/**
	 * 可使用战斗次数
	 */
	public int fightNum;
	
	/**
	 * 下次刷新时间
	 */
	public int flushTime;
	
	@Override
	public void write() {
		writeInt(fightNum);
		writeInt(flushTime);
	}
}
