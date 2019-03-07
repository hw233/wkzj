package com.jtang.gameserver.module.extapp.randomreward.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.randomreward.handler.response.GetRewardResponse;
import com.jtang.gameserver.module.extapp.randomreward.handler.response.RandomRewardResponse;

public interface RandomRewardFacade {

	/**
	 * 获取奖励信息
	 * @param actorId
	 * @return
	 */
	TResult<RandomRewardResponse> getInfo(long actorId);

	/**
	 * 领奖
	 * @param actorId
	 * @param id
	 * @return
	 */
	TResult<GetRewardResponse> getReward(long actorId, int id);

}
