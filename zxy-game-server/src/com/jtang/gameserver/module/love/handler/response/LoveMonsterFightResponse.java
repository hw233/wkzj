package com.jtang.gameserver.module.love.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;

public class LoveMonsterFightResponse extends IoBufferSerializer {

	
	/**
	 * 战斗信息
	 */
	public FightData data;
	
	/**
	 * 掉落奖励
	 */
	public List<RewardObject> list;
	
	public LoveMonsterFightResponse(FightData data, List<RewardObject> list){
		this.data = data;
		this.list = list;
	}
	
	@Override
	public void write() {
		writeBytes(data.getBytes());
		writeShort((short) list.size());
		for(RewardObject reward : list){
			writeBytes(reward.getBytes());
		}
	}
}
