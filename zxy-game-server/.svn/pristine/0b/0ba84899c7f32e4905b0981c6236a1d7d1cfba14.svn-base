package com.jtang.gameserver.module.adventures.bable.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableAutoResponse;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableDataResponse;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableExcangeGoodsResponse;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableSkipResponse;


public interface BableFacade {

	/**
	 * 获取已进入登天塔次数
	 * @param actorId
	 * @return
	 */
	public int getHasEnterTimes(long actorId);

	/**
	 * 获取登天塔信息
	 * @param actorId
	 * @param bableType 
	 * @return
	 */
	public TResult<BableDataResponse> getBableInfo(long actorId, int bableType);

	/**
	 * 选择登天塔
	 * @param actorId
	 * @param bableType
	 * @return
	 */
	public TResult<BableDataResponse> choiceBable(long actorId, int bableType);

	/**
	 * 开始战斗
	 * @param actorId
	 * @return
	 */
	public Result startBattle(long actorId);

	/**
	 * 兑换物品
	 * @param actorId
	 * @param exchangeId
	 * @param exchangeNum
	 * @return
	 */
	public TResult<BableExcangeGoodsResponse> exchange(long actorId, int exchangeId, int exchangeNum);

	/**
	 * 请求兑换物品列表
	 * @param actorId
	 * @return
	 */
	public TResult<BableExcangeGoodsResponse> getExchangeList(long actorId);

	/**
	 * 重置登天塔
	 * @param actorId
	 * @return
	 */
	public TResult<BableDataResponse> resetBable(long actorId);

	/**
	 * 自动登塔领取奖励
	 * @param actorId
	 * @param bableType 
	 * @return
	 */
	public TResult<BableSkipResponse> skipFloorReward(long actorId);
	/**
	 * 自动登塔
	 * @param actorId
	 * @param bableType
	 * @return
	 */
	public TResult<BableAutoResponse> skipFloor(long actorId, int bableType, int useGoodsId);
	
	
}
