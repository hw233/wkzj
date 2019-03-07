package com.jtang.gameserver.module.demon.dao;

import java.util.List;

import com.jtang.gameserver.dbproxy.entity.DemonRank;

public interface DemonRankDao {
	DemonRank get(long actorId);
	
	boolean update(List<DemonRank> demonRank);
	
	List<DemonRank> getByCondition(long difficult, int rankNum);
}
