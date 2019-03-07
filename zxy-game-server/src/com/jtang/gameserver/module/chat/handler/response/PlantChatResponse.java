package com.jtang.gameserver.module.chat.handler.response;

import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.module.icon.model.IconVO;

public class PlantChatResponse extends ChatResponse {

	/**
	 * 植物id
	 */
	public int plantId;
	
	/**
	 * 奖励列表
	 */
	public RewardObject rewardObject;
	
	
	public PlantChatResponse(int msgType, String sendName, long actorId,
			int level, int vipLevel,int plantId,RewardObject rewardObject,IconVO iconVO) {
		super(msgType, sendName, actorId, level, vipLevel,iconVO);
		this.plantId = plantId;
		this.rewardObject = rewardObject;
	}
	
	@Override
	public void write() {
		super.write();
		writeInt(plantId);
		writeBytes(rewardObject.getBytes());
	}

}
