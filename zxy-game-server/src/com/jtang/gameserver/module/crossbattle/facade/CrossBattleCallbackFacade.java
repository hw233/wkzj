package com.jtang.gameserver.module.crossbattle.facade;

import java.util.List;

import com.jiatang.common.crossbattle.model.ActorCrossData;
import com.jiatang.common.crossbattle.model.DayEndRewardVO;
import com.jiatang.common.crossbattle.request.SignupG2W;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.battle.model.BattleResult;

public interface CrossBattleCallbackFacade {
	
	/**
	 * 攻击角色回调结果 
	 * @param actorId
	 * @param targetActorId
	 * @param selfFightModel
	 * @param targetFightModel
	 * @param selfMorale
	 * @param targetMorale
	 * @return
	 */
	Result attactActorCallBack(long actorId, long targetActorId, byte[] selfFightModel, byte[] targetFightModel, int selfMorale, int targetMorale);

	/**
	 * 获取战斗动画
	 * @param actorId
	 * @return
	 */
	BattleResult pickBattleResult(long actorId);

	/**
	 * 设置跨服战开始结束
	 * @param start
	 */
	void setCrossBattleState(byte start);
	
	boolean isStart();
	
	ActorCrossData getCrossData(long actorId);
	
	TResult<SignupG2W> getSignupData(int powerRank);

	/**
	 * 积分兑换增加奖励
	 * @param actorId
	 * @param list
	 * @return
	 */
	Result exchangePoint(long actorId, List<RewardObject> list);

	/**
	 * 攻击玩家
	 * @return
	 */
	Result attackPlayer();

	/**
	 * 每日比赛结束奖励结果
	 * @param actorId
	 * @param endReward
	 * @return
	 */
	Result dayBattleEndRewardResult(long actorId, DayEndRewardVO endReward);

	/**
	 * 发放赛季结束奖励
	 * @param actorId
	 * @param rewardObjects
	 * @param serverScoreRank 
	 */
	void allEndReward(long actorId, String rewardObjects, int serverScoreRank);
	
	/**
	 * 是否参与了跨服战
	 * @param actorId
	 * @return
	 */
	boolean isInCrossBattle(long actorId);
	
}
