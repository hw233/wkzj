package com.jtang.gameserver.module.goods.handler;

/**
 * 仓库命令
 * @author 0x737263
 *
 */
public interface GoodsCmd {

	/**
	 * 获取物品列表
	 * <pre>
	 * 请求:{@code Request}
	 * 响应:{@code GoodsListResponse}
	 * </pre>
	 */
	byte GET_LIST = 1;
	
	/**
	 * 推送物品属性更新
	 * <pre>
	 * 请求:{@code Request}
	 * 响应:{@code GoodsAttributeResponse}
	 * 推送:{@code GoodsAttributeResponse}
	 * </pre>
	 */
	byte PUSH_GOODS_ATTRUBITE = 2;
	
	/**
	 * 使用物品
	 * <pre>
	 * 请求:{@code UseGoodsRequest}
	 * 响应:{@code UseGoodsResponse}
	 * </pre>
	 */
	byte USE_GOODS = 3;
	
	/**
	 * 出售物品
	 * 请求:{@code SellGoodsRequest}
	 * 返回:{@code SellGoodsResponse}
	 */
	byte SELL_GOODS = 4;
	
	/**
	 * 开始合成
	 * 请求:{@code StartComposeRequest}
	 * 返回:{@code StartComposeResponse}
	 */
	byte COMPOSE_GOODS = 5;
	
	
}
