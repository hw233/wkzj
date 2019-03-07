package com.jtang.gameserver.module.extapp.basin.dao;

import com.jtang.gameserver.dbproxy.entity.Basin;

public interface BasinDao {

	public Basin get(long actorId);

	public void update(Basin basin);

}
