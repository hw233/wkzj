package com.jtang.gameserver.module.adventures.shop.trader.handler;

public interface TraderCmd {

	/**
	 * 获取店铺信息
	 * 请求{@code ShopInfoRequest}
	 * 返回{@code ShopInfoResponse}
	 * 推送{@code ShopInfoResponse}
	 */
	byte TRADER_INFO = 1;
	
	/**
	 * 购买
	 * 请求{@code ShopRequest}
	 * 返回{@code Response}
	 */
	byte TRADER_BUY = 2;
	
	/**
	 * 刷新商品列表
	 * 请求{@code ShopFlushRequest}
	 * 返回{@code ShopInfoResponse}
	 */
	byte TRADER_FLUSH = 3;
	
	/**
	 * 购买永久
	 * 请求{@code ShopPermanentRequest}
	 * 返回{@code ShopInfoResponse}
	 */
	byte TRADER_PERMANENT = 4;
	
	/**
	 * 推送临时开启
	 * 推送:{code ShopInfoResponse}
	 */
	byte PUSH_DURATION = 5;
	
	/**
	 * 获取商店开启信息
	 * 请求:{@code Request}
	 * 返回:{@code ShopOpenResponse}
	 */
	byte GET_SHOP_OPEN_INFO = 6;
	
	/**
	 * 推送商店状态变更
	 * 推送:{@code ShopOpenResponse}
	 */
	byte PUSH_SHOP_STATE = 7;
	
}
