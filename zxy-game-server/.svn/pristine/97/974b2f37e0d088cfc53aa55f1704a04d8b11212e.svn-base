package com.jtang.gameserver.module.ladder.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

public class LadderRewardResponse extends IoBufferSerializer {
	
	/**
	 * 本次排名
	 */
	public int rank;

	/**
	 * 历史最高排名
	 */
	public int historyRank;
	
	/**
	 * 奖励列表
	 */
	public List<RewardObject> rewardList = new ArrayList<>();
	
	@Override
	public void write() {
		writeInt(rank);
		writeInt(historyRank);
		writeShort((short)rewardList.size());
		for(RewardObject rewardObject:rewardList){
			writeBytes(rewardObject.getBytes());
		}
	}
}
