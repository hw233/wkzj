package com.jtang.gameserver.module.praise.facade;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.praise.handler.response.PraiseDataResponse;
import com.jtang.gameserver.module.praise.type.PraiseRewardType;

public interface PraiseFacade {
	
	/**
	 * 获取数据
	 * @param actorId
	 * @return
	 */
	TResult<PraiseDataResponse> getPraiseData(long actorId);
	
	/**
	 * 领取奖励
	 * @param actorId
	 * @param commentRewardType
	 * @return
	 */
	TResult<List<RewardObject>> getReward(long actorId, PraiseRewardType praiseRewardType);
}
