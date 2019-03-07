package com.jtang.gameserver.module.extapp.monthcard.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.monthcard.handler.response.MonthCardResponse;

public interface MonthCardFacade {

	/**
	 * 请求月卡信息
	 * @param actorId
	 * @return
	 */
	TResult<MonthCardResponse> getInfo(long actorId);

	/**
	 * 获取奖励
	 * @param actorId
	 * @return
	 */
	Result getReward(long actorId);

	/**
	 * 获取终身卡奖励
	 * @param actorId
	 * @return
	 */
	Result getlifelongReward(long actorId);

	/**
	 * 获取月卡剩余天数
	 * @param actorId
	 * @return
	 */
	public int getDay(long actorId);

	/**
	 * 获取年卡奖励
	 * @param session
	 * @return
	 */
	Result getYearReward(long actorId);

}
