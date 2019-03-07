package com.jtang.gameserver.module.refine.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class RefineInfoReponse extends IoBufferSerializer {

	/**
	 * 精炼室的等级
	 */
	public int roomLevel;
	
	public RefineInfoReponse(int roomLevel) {
		this.roomLevel = roomLevel;
	}

	@Override
	public void write() {
		writeInt(roomLevel);

	}

}
