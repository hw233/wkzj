package com.jtang.gameserver.module.adventures.shop.shop.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Shop;

public interface ShopFacade {

	/**
	 * 获取购买信息
	 * */
	public Shop getShops(long actorId);

	
	/**
	 * 购买商品
	 * @param num 
	 * */
	public TResult<Integer> buy(long actorId, int shopId, int num);
}
