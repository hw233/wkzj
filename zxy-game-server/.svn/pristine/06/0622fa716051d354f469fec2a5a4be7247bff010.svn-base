package com.jtang.gameserver.module.crossbattle.dao;

import com.jtang.gameserver.dbproxy.entity.CrossBattleActorRewardFlag;
import com.jtang.gameserver.dbproxy.entity.CrossBattleReward;

public interface CrossBattleDao {

	/**
	 * 获取赛季结束奖励
	 * @return
	 */
	public CrossBattleReward getReward();

	/**
	 * 获取玩家的领奖记录
	 * @param actorId
	 * @return
	 */
	public CrossBattleActorRewardFlag get(long actorId);

	/**
	 * 更新领奖信息
	 * @param actorReward
	 */
	public void update(CrossBattleActorRewardFlag actorReward);

	/**
	 * 更新赛季结束奖励
	 * @param rewardObjects
	 */
	public void update(String rewardObjects);

}
