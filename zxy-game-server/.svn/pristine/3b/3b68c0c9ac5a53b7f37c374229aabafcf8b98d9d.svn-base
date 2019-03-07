package com.jtang.gameserver.module.extapp.randomreward.handler.response;

import java.util.Collection;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.extapp.randomreward.model.RewardVO;

public class RandomRewardResponse extends IoBufferSerializer {

	/**
	 * 奖励信息
	 */
	public Collection<RewardVO> list;
	
	public RandomRewardResponse(Collection<RewardVO> collection){
		this.list = collection;
	}
	
	@Override
	public void write() {
		writeShort((short) list.size());
		for(RewardVO rewardVO:list){
			writeBytes(rewardVO.getBytes());
		}
	}
}
