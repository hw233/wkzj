package com.jtang.gameserver.module.praise.dao;

import com.jtang.gameserver.dbproxy.entity.Praise;

public interface PraiseDao {
	Praise get(long actorId);
	
	boolean update(Praise praise);
}	
