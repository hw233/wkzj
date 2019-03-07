package com.jtang.gameserver.module.app.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 领奖回复
 * @author ludd
 *
 */
public class GetRewardResponse extends IoBufferSerializer {

	/**
	 * 奖励列表
	 */
	private List<RewardObject> rewardObjectList;
	
	public GetRewardResponse(List<RewardObject> rewardObjectList) {
		this.rewardObjectList = rewardObjectList;
	}

	@Override
	public void write() {
		this.writeShort((short) rewardObjectList.size());
		for (RewardObject reward : rewardObjectList) {
			this.writeInt(reward.rewardType.getCode());
			this.writeInt(reward.id);
			this.writeInt(reward.num);
		}
	}

}
