package com.jtang.gameserver.module.demon.dao;

import com.jtang.gameserver.dbproxy.entity.Demon;

public interface DemonDao {
	Demon get(long actorId);
	
	boolean update(Demon demon);
}
