package com.jtang.gameserver.module.lineup.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 推送格子解锁
 * @author vinceruan
 *
 */
public class PushLineupUnlockResponse extends IoBufferSerializer {
	private int activedGridCount;

	@Override
	public void write() {
		writeByte((byte)activedGridCount);
	}
	
	public PushLineupUnlockResponse(int activedGridCount) {
		this.activedGridCount = activedGridCount;
	}
}
