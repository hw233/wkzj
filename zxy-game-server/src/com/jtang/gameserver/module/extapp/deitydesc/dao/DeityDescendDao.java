package com.jtang.gameserver.module.extapp.deitydesc.dao;

import com.jtang.gameserver.dbproxy.entity.DeityDescend;

public interface DeityDescendDao {

	/**
	 * 
	 * @param actorId
	 * @return
	 */
	DeityDescend get(long actorId);
	
	/**
	 * 
	 * @param deityDescend
	 * @return
	 */
	boolean update(DeityDescend deityDescend);
}
