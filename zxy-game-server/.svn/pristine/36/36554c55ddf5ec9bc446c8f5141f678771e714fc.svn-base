package com.jtang.gameserver.module.herobook.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.herobook.handler.response.HeroBookResponse;
import com.jtang.gameserver.module.herobook.handler.response.HeroBookRewardResponse;

public interface HeroBookFacade {
	/**
	 * 获取图鉴数据
	 * @param actorId
	 * @return
	 */
	HeroBookResponse getHeroBookData(long actorId);
	
	/**
	 * 领取奖励
	 * @param actorId
	 * @return
	 */
	TResult<HeroBookRewardResponse> getReward(long actorId, int orderId);
}
