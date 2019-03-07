package com.jtang.gameserver.module.demon.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.demon.model.DemonVO;

public class PlayerJoinResponse extends IoBufferSerializer {
	/**
	 * 玩家降魔数据
	 */
	private DemonVO demonVO;
	
	public PlayerJoinResponse(DemonVO demonVO) {
		this.demonVO = demonVO;
	}
	@Override
	public void write() {
		this.writeBytes(demonVO.getBytes());
	}

}
