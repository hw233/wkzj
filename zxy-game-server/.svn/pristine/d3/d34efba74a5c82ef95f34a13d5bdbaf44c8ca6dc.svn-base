package com.jtang.gameserver.module.power.handler;

/**
 * 最强势力排行榜协议命令
 * @author 0x737263
 *
 */
public interface PowerCmd {
	
	/**
	 * 获取排行榜信息
	 * 请求:{@code Request}
	 * 返回:{@code PowerInfoResponse}
	 */
	byte GET_POWER_INFO = 1;
	
	/**
	 * 排行榜挑战
	 * 请求:{@code PowerFightRequest}
	 * 返回:{@code Resposne}
	 * 推送:{@code PowerFightResponse}
	 */
	byte POWER_FIGHT = 2;
	
	/**
	 * 补满挑战次数
	 * 请求:{@code Request}
	 * 返回:{@code PowerFlushResponse}
	 */
	byte BUY_FIGHT_NUM = 3;
	
	/**
	 * 兑换奖励
	 * 请求:{@code PowerExchangeRequest}
	 * 返回:{@code Response}
	 */
	byte EXCHANGE = 4;
	
	/**
	 * 推送跨天
	 * 推送:{@code Response}
	 */
	byte PUSH_POWER_RESET = 5;
	
	/**
	 * 请求固定排行榜
	 * 请求:{@code Request}
	 * 返回:{@code RankListResponse}
	 */
	byte GET_TOP_RANK = 6;
	
	/**
	 * 获取可挑战的人
	 * 请求:{@code Request}
	 * 返回:{@code RankListResponse}
	 */
	byte GET_FIGHT_RANK = 7;
	
	/**
	 * 获取商店信息
	 * 请求:{@code Request}
	 * 返回:{@code PowerShopInfoResponse}
	 */
	byte GET_POWER_SHOP = 8;
	
	/**
	 * 刷新商品列表
	 * 请求:{@code Request}
	 * 返回:{@code PowerShopInfoResponse}
	 */
	byte SHOP_FLUSH = 9;
	
}
