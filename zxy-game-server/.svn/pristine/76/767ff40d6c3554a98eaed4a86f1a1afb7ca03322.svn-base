package com.jtang.gameserver.module.notice.facade;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.module.battle.model.BattleResult;

public interface NoticeFacade {
	
	/**
	 * 最强势力挑战成功
	 */
	void broadcastSnatchPowerRank(BattleResult battleResult);
	
	/**
	 * 集众降魔抢夺积分公告
	 * @param receiverActorId 接受消息人id
	 * @param sendActorId 抢夺人id
	 */
	void broadcastDemonSnatch(long receiverActorId,long sendActorId);
	
	/**
	 * 跨服战赛季结束
	 */
	void broadcastCrossBattleEnd(int rank,List<RewardObject> rewardObjct);
	
}
