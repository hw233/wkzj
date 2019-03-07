package com.jtang.gameserver.module.treasure.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.treasure.model.TreasureVO;

public interface TreasureFacade {

	/**
	 * 获取寻宝信息
	 * 
	 * @param actorId
	 * @return
	 */
	public TResult<TreasureVO> getTreasure(long actorId);

	/**
	 * 走一步
	 * 
	 * @param actorId
	 * @return
	 */
	public Result move(long actorId);

	/**
	 * 兑换奖励
	 * @param actorId
	 * @param exchangeId
	 * @param num
	 * @return
	 */
	public TResult<Integer> exchangeReward(long actorId, int exchangeId, int num);

	/**
	 * 获取兑换物品数量
	 * @param actorId
	 * @return 
	 */
	public TResult<Integer> exchangeGoods(long actorId);
	

}
