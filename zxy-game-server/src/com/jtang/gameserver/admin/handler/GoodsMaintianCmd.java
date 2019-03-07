package com.jtang.gameserver.admin.handler;

public interface GoodsMaintianCmd {

	
	/**
	 * 添加物品
	 * 请求: {@code GiveGoodsRequest}
	 * 响应: {@code Response}
	 */
	byte ADD_GOODS = 1;
	
	
	
	/**
	 * 删除物品
	 * 请求:{@code DeleteGoodsRequest}
	 * 相应:{@code Response}
	 */
	byte DEL_GOODS = 2;
	
	/**
	 * 添加goodsConfig中所有物品
	 * 请求:{@code Request}
	 * 相应:{@code Response}
	 */
	byte ADD_ALL_GOODS = 3;
}
