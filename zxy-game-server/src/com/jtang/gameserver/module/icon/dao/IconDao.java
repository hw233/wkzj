package com.jtang.gameserver.module.icon.dao;

import com.jtang.gameserver.dbproxy.entity.Icon;

public interface IconDao {

	/**
	 * @param actorId
	 * @return
	 */
	public Icon get(long actorId);

	/**
	 * 
	 * @param icon
	 */
	public void update(Icon icon);

}
