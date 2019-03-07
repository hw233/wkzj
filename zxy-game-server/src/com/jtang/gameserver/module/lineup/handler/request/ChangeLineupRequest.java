package com.jtang.gameserver.module.lineup.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class ChangeLineupRequest extends IoBufferSerializer {

	/**
	 * 更换阵型的索引
	 */
	public int linequpIndex;
	
	public ChangeLineupRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.linequpIndex = readByte();
	}

}
