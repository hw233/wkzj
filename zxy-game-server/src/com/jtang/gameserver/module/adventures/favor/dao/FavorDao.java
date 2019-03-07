package com.jtang.gameserver.module.adventures.favor.dao;

import com.jtang.gameserver.dbproxy.entity.Favor;

/**
 * 
 * @author 0x737263
 *
 */
public interface FavorDao {

	Favor get(long actorId);

	void update(Favor favor);


}
