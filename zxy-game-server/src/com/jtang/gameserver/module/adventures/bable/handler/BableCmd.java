package com.jtang.gameserver.module.adventures.bable.handler;



/**
 * 通天塔协议定义
 * @author 0x737263
 *
 */
public interface BableCmd {
	
	/**
	 * 获取登天塔进度信息
	 * 请求: {@code BableDataRequest}
	 * 响应: {@code BableDataResponse}
	 */
	byte GET_INFO = 1;
	
	/**
	 * 选择登天塔
	 * 请求:{@code BableDataRequest}
	 * 返回:{@code BableDataResponse}
	 */
	byte CHOICE_BABLE = 2;
	
	/**
	 * 开始战斗
	 * <pre>
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 * 推送:{@code BableBattleResultResponse}
	 * </pre>
	 */
	byte START_BATTLE = 3;
	
	/**
	 * 兑换永久物品
	 * <pre>
	 * 请求:{@code ExchangeGoodsRequst}
	 * 响应：{@code BableExcangeGoodsResponse}
	 * 同步:物品
	 * </pre>
	 */
	byte EXCHANGE_GOODS = 4;
	
	/**
	 * 请求永久兑换物品数据
	 * 请求: {@code Request}
	 * 响应: {@code BableExcangeGoodsResponse}
	 */
	byte GET_EXCHANGE_GOODS_DATA = 5;
	
	/**
	 * 获取所有登天塔排名
	 * 请求:{@code Request}
	 * 响应:{@code BableRankResponse}
	 */
	byte GET_RANK = 7;
	
	/**
	 * 重置当前登天塔
	 * 请求:{@code Request}
	 * 响应:{@code BableDataResponse}
	 */
	byte RESET_BABLE = 8;
	
	/**
	 * 自动登塔获取奖励
	 * 请求:{@code Request}
	 * 响应:{@code BableSkipResponse}
	 */
	byte SKIP_FLOOR = 9;
	
	/**
	 * 推送通天币变更
	 * 推送:{@code BableStarResponse}
	 */
	byte PUSH_BABLE_STAR = 10;
	
	/**
	 * 自动登塔
	 * 请求:{@code BableAutoRequest}
	 * 返回:{@code BableAutoResponse}
	 */
	byte AUTO_BABLE = 11;
	
	/**
	 * 推送跨天
	 * 推送:{@code Response}
	 */
	byte PUSH_BABLE_RESET = 12;
	
	
}
