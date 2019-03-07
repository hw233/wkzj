package com.jtang.gameserver.module.snatch.dao;

import com.jtang.gameserver.dbproxy.entity.Snatch;

public interface SnatchDao {

	/**
	 * 获取抢夺信息
	 * @param actorId
	 * @return
	 */
	Snatch get(long actorId);
	
	/**
	 * 更新抢夺信息
	 * @param snatch
	 * @return
	 */
	boolean update(Snatch snatch);
}
