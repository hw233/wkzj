package com.jtang.gameserver.module.crossbattle.facade;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.result.TResult;

public interface CrossBattleRewardFacade {

	/**
	 * 获取赛季结束奖励 
	 * @param actorId
	 * @return
	 */
	TResult<List<RewardObject>> getReward(long actorId);

	/**
	 * 是否可以领取奖励
	 * @param actorId
	 * @return
	 */
	TResult<Integer> isGet(long actorId);

}
