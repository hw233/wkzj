package com.jtang.gameserver.module.adventures.shop.shop.handler;

public interface ShopCmd {
	/**
	 * 获取shop信息
	 * 请求:{@code Request}
	 * 响应:{@code ShopInfoResponse}
	 * */
	byte SHOP_INFO = 1;
	
	
	/**
	 * 购买
	 * 请求：{@code ShopRequest}
	 * 响应:{@code Response}
	 * */
	byte BUY=2;
	
	
	/**
	 * 推送购买信息
	 * 推送:{@code ShopInfoResponse}
	 * */
	byte SHOP_BUY_INFO = 3;
	
	/**
	 * 获取黑市商店信息
	 * 请求:{@code Request}
	 * 返回:{@code ExchangeListResponse}
	 * 推送:{@code ExchangeListResponse}
	 */
	byte BLACK_SHOP_INFO = 4;
	
	/**
	 * 购买黑市商店商品
	 * 请求:{@code BlackShopRequest}
	 * 返回:{@code Response}
	 */
	byte BLACK_SHOP_BUY = 5;
	
	/**
	 * 刷新黑市商店商品
	 * 请求:{@code Request}
	 * 返回:{@code ExchangeListResponse}
	 */
	byte BLACK_SHOP_FLUSH = 6;
	
}
