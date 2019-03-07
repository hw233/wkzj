package com.jtang.gameserver.module.adventures.shop.shop.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.shop.shop.handler.response.ExchangeListResponse;

public interface BlackShopFacade {

	/**
	 * 商店兑换物品
	 * @param actorId
	 * @param type		ExchangeType
	 * @param cfgId
	 * @return
	 */
	Result exchange(long actorId,int cfgId);

	/**
	 * 获取商店兑换列表
	 * @param actorId
	 * @return
	 */
	TResult<ExchangeListResponse> getExchangeList(long actorId);

	/**
	 * 刷新兑换列表
	 * @param actorId
	 * @return
	 */
	TResult<ExchangeListResponse> flushExchange(long actorId);
	
}
