package com.jtang.gameserver.module.adventures.shop.trader.facade;

import java.util.Map;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.shop.trader.handler.response.ShopInfoResponse;
import com.jtang.gameserver.module.adventures.shop.trader.type.ShopType;

public interface TraderFacade {

	/**
	 * 获取商店信息
	 * @param shopType
	 * @return
	 */
	public TResult<ShopInfoResponse> getInfo(ShopType shopType,long actorId);

	/**
	 * 购买商品
	 * @param shopType
	 * @param shopId
	 * @param num
	 * @return
	 */
	public Result shopBuy(ShopType shopType,long actorId, int shopId, int num);

	/**
	 * 刷新商品列表
	 * @param shopType
	 * @return
	 */
	public TResult<ShopInfoResponse> shopFlush(ShopType shopType,long actorId);

	/**
	 * 购买永久使用权限
	 * @param shopType
	 * @return
	 */
	public TResult<ShopInfoResponse> buyPermanent(ShopType shopType,long actorId);

	/**
	 * 获取各个商店开启信息
	 * @param actorId
	 * @return
	 */
	public TResult<Map<Integer, Integer>> getOpenInfo(long actorId);

}
