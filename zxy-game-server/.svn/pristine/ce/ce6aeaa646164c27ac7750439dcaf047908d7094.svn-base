package com.jtang.gameserver.module.luckstar.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.luckstar.handler.response.LuckStarRewardResponse;
import com.jtang.gameserver.module.luckstar.module.LuckStarVO;

public interface LuckStarFacade {

	/**
	 * 获取幸运星信息
	 * @param actorId
	 * @return
	 */
	public TResult<LuckStarVO> getLuckStar(long actorId);

	/**
	 * 获取幸运星奖励
	 * @param actorId
	 * @return
	 */
	public TResult<LuckStarRewardResponse> getReward(long actorId);

}
