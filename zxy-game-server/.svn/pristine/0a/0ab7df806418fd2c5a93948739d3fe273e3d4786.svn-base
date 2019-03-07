package com.jtang.gameserver.module.love.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.love.handler.response.LoveShopInfoResponse;

public interface LoveShopFacade {
	/**
	 * 获取商店信息
	 * @param shopType
	 * @return
	 */
	public TResult<LoveShopInfoResponse> getInfo(long actorId);

	/**
	 * 购买商品
	 * @param shopType
	 * @param shopId
	 * @param num
	 * @return
	 */
	public Result shopBuy(long actorId, int shopId, int num);

	/**
	 * 刷新商品列表
	 * @param shopType
	 * @return
	 */
	public TResult<LoveShopInfoResponse> shopFlush(long actorId);

	/**
	 * 购买永久使用权限
	 * @param shopType
	 * @return
	 */
	public TResult<LoveShopInfoResponse> buyPermanent(long actorId);
	
	/**
	 * 离婚调用接口
	 */
	public Result unMarry(long actorId);
}
