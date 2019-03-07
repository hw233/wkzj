package com.jtang.gameserver.module.treasure.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class TreasureStateResponse extends IoBufferSerializer {

	/**
	 * 寻宝状态 0.关闭 1.开启
	 */
	public int state;

	public TreasureStateResponse(int state) {
		this.state = state;
	}

	@Override
	public void write() {
		writeInt(state);
	}

}
