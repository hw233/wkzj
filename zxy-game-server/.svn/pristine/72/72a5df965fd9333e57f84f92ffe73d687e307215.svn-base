package com.jtang.gameserver.module.extapp.basin.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.basin.handler.response.BasinResponse;
import com.jtang.gameserver.module.extapp.basin.handler.response.BasinStateResponse;

public interface BasinFacade {

	/**
	 * 获取信息
	 * @param actorId
	 * @return
	 */
	public TResult<BasinResponse> getInfo(long actorId);

	/**
	 * 领取奖励
	 * @param actorId
	 * @param recharge 
	 * @return
	 */
	public Result getReward(long actorId, int recharge);

	/**
	 * 获取活动状态
	 * @return
	 */
	public TResult<BasinStateResponse> getState();

}
