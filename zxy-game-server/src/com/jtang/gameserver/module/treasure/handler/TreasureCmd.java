package com.jtang.gameserver.module.treasure.handler;


public interface TreasureCmd {

	/**
	 * 获取寻宝信息
	 * 请求:{@code Request}
	 * 响应:{@code TreasureResponse}
	 */
	byte GET_TREASURE = 1;
	
	/**
	 * 走一步
	 * 请求:{@code Request}
	 * 推送:{@code TreasureFightResponse}
	 */
	byte MOVE = 2;
	
	/**
	 * 寻宝状态推送
	 * 推送:{@code TreasureStateResponse} 
	 */
	byte PUSH_TREASURE_STATE = 3;
	
	/**
	 * 兑换星光碎片
	 * 请求:{@code TreasureExchangeRequest}
	 * 返回"{@code TreasureExchangeResponse}
	 */
	byte EXCHANGE_REWARD = 4;
	
	/**
	 * 获取星光碎片数量
	 * 请求:{@code Request}
	 * 返回:{@code TreasureGoodsResponse}
	 */
	byte EXCHANGE_GOODS = 5;
	
}
