package com.jtang.gameserver.module.extapp.randomreward.handler.response;

import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.extapp.randomreward.model.RewardVO;

public class GetRewardResponse extends IoBufferSerializer {

	/**
	 * 当前小人的状态
	 */
	public RewardVO rewardVO;
	
	/**
	 * 领取到的奖励
	 * @param rewardVO
	 */
	public Map<Integer,Integer> rewardMap;
	
	public GetRewardResponse(RewardVO rewardVO,Map<Integer,Integer> rewardMap){
		this.rewardVO = rewardVO;
		this.rewardMap = rewardMap;
	}
	
	@Override
	public void write() {
		writeBytes(rewardVO.getBytes());
		writeIntMap(rewardMap);
	}
}
