package com.jtang.gameserver.module.smelt.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.smelt.handler.response.SmeltInfoResponse;
import com.jtang.gameserver.module.smelt.handler.response.SmeltResponse;

public interface SmeltFacade {

	/**
	 * 仙人/魂魄转换
	 * @param actorId
	 * @param heroId
	 * @param num
	 * @return
	 */
	TResult<SmeltResponse> convert(long actorId, int heroId, int num);

	/**
	 * 获取兑换信息
	 * @param actorId
	 * @return
	 */
	TResult<SmeltInfoResponse> getExchangeInfo(long actorId);

	/**
	 * 进行兑换
	 * @param actorId
	 * @param heroId
	 * @param num
	 * @return
	 */
	Result exchange(long actorId, int heroId, int num);

}
