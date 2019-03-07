package com.jtang.gameserver.module.adventures.shop.vipshop.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.shop.vipshop.handler.response.VipShopResponse;

public interface VipShopFacade {

	/**
	 * 获取商店购买信息
	 * @param actorId
	 * @return
	 */
	TResult<VipShopResponse> getInfo(long actorId);

	/**
	 * 购买商品
	 * @param actorId
	 * @param id
	 * @param num
	 * @return
	 */
	Result buy(long actorId, int id, int num);

}
