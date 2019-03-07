package com.jtang.worldserver.module.crossbattle.dao;

import java.util.List;

import com.jtang.worldserver.dbproxy.entity.CrossBattleActor;

public interface CrossBattleActorDao {

	/**
	 * 获取玩家贡献点信息
	 * @param actorId
	 * @param serverId
	 * @return
	 */
	CrossBattleActor getCrossBattleActor(long actorId,int serverId);
	
	/**
	 * 更新玩家贡献点
	 * @param crossBattleActor
	 */
	void update(CrossBattleActor crossBattleActor);
	
	void update(List<CrossBattleActor> crossBattleActors);
}
