package com.jtang.gameserver.module.demon.helper;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.dbproxy.entity.DemonRank;

public class DemonHelper {
	/**
	 * 记录本次排行榜及奖励信息
	 * @param demonRank
	 * @param rank
	 * @param difficult
	 * @param isWin
	 * @param feats
	 * @param rewardScore
	 * @param firstDemonReward
	 * @param featsRankReward
	 * @param featsTotalReward
	 * @param useTicketReward
	 */
	public static void recordDemonRank(DemonRank demonRank, int rank, int difficult, byte isWin, long feats, int rewardScore,
			List<RewardObject> firstDemonReward, List<RewardObject> featsRankReward, List<RewardObject> featsTotalReward, List<RewardObject> useTicketReward) {
		demonRank.lastRank = rank;
		demonRank.lastDifficult = difficult;
		demonRank.lastIsWin = isWin;
		demonRank.lastFeats = feats;
		demonRank.lastRewardScore = rewardScore;
		demonRank.setFeatsRankReward(featsRankReward);
		demonRank.setFirstDemonReward(firstDemonReward);
		demonRank.setFeatsTotalReward(featsTotalReward);
		demonRank.setUseTicketReward(useTicketReward);
	}
}
