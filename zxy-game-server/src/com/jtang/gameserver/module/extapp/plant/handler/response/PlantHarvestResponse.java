package com.jtang.gameserver.module.extapp.plant.handler.response;

import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.extapp.plant.module.PlantVO;

public class PlantHarvestResponse extends IoBufferSerializer {

	/**
	 * 种植vo
	 */
	public PlantVO plantVO;

	/**
	 * 固定奖励列表
	 */
	public RewardObject mastReward;
	
	/**
	 * 额外奖励列表
	 */
	public RewardObject extReward;
	
	/**
	 * 保底奖励列表
	 */
	public RewardObject reward;
	
	public PlantHarvestResponse(PlantVO plantVO,RewardObject mastReward,RewardObject extReward,RewardObject reward){
		this.plantVO = plantVO;
		this.mastReward = mastReward;
		if(extReward == null){
			this.extReward = new RewardObject(RewardType.NONE,0,0);
		}else{
			this.extReward = extReward;
		}
		if(reward == null){
			this.reward = new RewardObject(RewardType.NONE,0,0);
		}else{
			this.reward = reward;
		}
	}
	
	@Override
	public void write() {
		writeBytes(plantVO.getBytes());
		writeBytes(mastReward.getBytes());
		writeBytes(extReward.getBytes());
		writeBytes(reward.getBytes());
	}
}
