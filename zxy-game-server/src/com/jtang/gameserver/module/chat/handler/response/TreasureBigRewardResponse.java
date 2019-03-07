package com.jtang.gameserver.module.chat.handler.response;

import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.module.icon.model.IconVO;

public class TreasureBigRewardResponse extends ChatResponse {

	/**
	 * 获得的奖励
	 */
	RewardObject rewardObject;

	public TreasureBigRewardResponse(int msgType, String sendName, long actorId, int level, int vipLevel, RewardObject rewardObject,IconVO iconVO) {
		super(msgType, sendName, actorId, level, vipLevel,iconVO);
		this.rewardObject = rewardObject;
	}
	
	@Override
	public void write() {
		super.write();
		writeBytes(rewardObject.getBytes());
	}

}
