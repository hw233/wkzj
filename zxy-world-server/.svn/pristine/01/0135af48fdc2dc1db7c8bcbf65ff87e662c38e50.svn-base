package com.jtang.worldserver.module.crossbattle.dao;

import java.util.List;

import com.jtang.worldserver.dbproxy.entity.CrossBattleHurtRank;

public interface CrossBattleHurtRankDao {
	
	/**
	 * 获取玩家伤害排行信息
	 * @param actorId
	 * @param serverId
	 * @return
	 */
	CrossBattleHurtRank getCrossBattleHurtRank(long actorId, int serverId);
	
	/**
	 * 获取某个服务器伤害排行信息
	 * @param serverId
	 * @return
	 */
	List<CrossBattleHurtRank> getCrossBattleServerHurtRank(int serverId);
	
	/**
	 * 更新玩家伤害排行
	 * @param crossBattleHurtRank
	 */
	void update(CrossBattleHurtRank crossBattleHurtRank);
	/**
	 * 更新玩家伤害排行
	 * @param crossBattleHurtRank
	 */
	void update(List<CrossBattleHurtRank> crossBattleHurtRanks);

}
