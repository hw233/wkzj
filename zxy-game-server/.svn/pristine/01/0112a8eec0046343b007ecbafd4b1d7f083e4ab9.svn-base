package com.jtang.gameserver.module.extapp.welkin.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class WelkinStateResponse extends IoBufferSerializer {

	/**
	 * 天宫状态
	 * 0.关闭 1.开启
	 */
	public int state;
	
	public WelkinStateResponse(int state) {
		this.state = state;
	}

	@Override
	public void write() {
		writeInt(state);
	}
}
