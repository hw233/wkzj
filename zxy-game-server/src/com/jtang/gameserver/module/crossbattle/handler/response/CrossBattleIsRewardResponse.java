package com.jtang.gameserver.module.crossbattle.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class CrossBattleIsRewardResponse extends IoBufferSerializer {
	
	/**
	 * 是否可以领取
	 * 0.不可以  1.可以
	 */
	public int isGet;
	
	public CrossBattleIsRewardResponse(int isGet){
		this.isGet = isGet;
	}
	
	@Override
	public void write() {
		writeInt(isGet);
	}
}
