package com.jtang.gameserver.module.snatch.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class SnatchFightNumResponse extends IoBufferSerializer {

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
