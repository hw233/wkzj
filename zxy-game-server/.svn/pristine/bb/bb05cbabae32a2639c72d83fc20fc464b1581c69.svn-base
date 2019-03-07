package com.jtang.gameserver.module.praise.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

public class PraiseRewardResponse extends IoBufferSerializer {

	/**
	 * 好评数据
	 */
	private PraiseDataResponse praiseDataResponse;
	
	/**
	 * 奖励列表
	 */
	private List<RewardObject> rewardList;
	
	
	public PraiseRewardResponse(PraiseDataResponse praiseDataResponse, List<RewardObject> rewardList) {
		super();
		this.praiseDataResponse = praiseDataResponse;
		this.rewardList = rewardList;
	}


	@Override
	public void write() {
		this.writeBytes(praiseDataResponse.getBytes());
		this.writeShort((short)rewardList.size());
		for (RewardObject rewardObject : rewardList) {
			this.writeBytes(rewardObject.getBytes());
		}
	}

}
