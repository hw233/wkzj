package com.jtang.gameserver.module.battle.dao;

import com.jtang.gameserver.dbproxy.entity.Battle;

public interface BattleDao {
	
	Battle get(long actorId);
	
	boolean update(long actorId);
}
