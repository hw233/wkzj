package com.jtang.gameserver.module.extapp.craftsman.dao;

import com.jtang.gameserver.dbproxy.entity.Craftsman;

public interface CraftsmanDao {
	/**
	 * 
	 * @param actorId
	 * @return
	 */
	Craftsman get(long actorId);
	
	/**
	 * 
	 * @param craftsman
	 * @return
	 */
	boolean update(Craftsman craftsman);
}
