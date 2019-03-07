package com.jtang.gameserver.module.crossbattle.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

public class CrossBattleRewardResponse extends IoBufferSerializer {

	/**
	 * 奖励列表
	 */
	public List<RewardObject> list;
	
	public CrossBattleRewardResponse(List<RewardObject> list) {
		this.list = list;
	}
	
	@Override
	public void write() {
		writeShort((short) list.size());
		for(RewardObject reward:list){
			writeBytes(reward.getBytes());
		}
	}

}
