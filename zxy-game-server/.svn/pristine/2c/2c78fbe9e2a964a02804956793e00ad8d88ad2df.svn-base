package com.jtang.gameserver.module.hole.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.hole.model.HoleVO;

public class HoleOpenResponse extends IoBufferSerializer {
	
	private HoleVO holeVO;

	public HoleOpenResponse(HoleVO holeVO){
		this.holeVO = holeVO;
	}
	
	@Override
	public void write() {
		writeBytes(holeVO.getBytes());
	}

	
}
