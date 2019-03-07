package com.jtang.gameserver.module.love.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;

public class LoveFightResponse extends IoBufferSerializer{

	/**
	 * 战斗数据
	 */
	public FightData data;
	
	/**
	 * 挑战的排名
	 */
	public int targetRank;
	
	public LoveFightResponse(FightData fightData,int targetRank){
		this.data = fightData;
		this.targetRank = targetRank;
	}
	
	@Override
	public void write() {
		writeBytes(data.getBytes());
		writeInt(targetRank);
	}
}
