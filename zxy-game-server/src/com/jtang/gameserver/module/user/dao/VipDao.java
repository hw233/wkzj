package com.jtang.gameserver.module.user.dao;

import com.jtang.gameserver.dbproxy.entity.Vip;

public interface VipDao {
	/**
	 * 获取
	 * @param actorId
	 * @return
	 */
	public Vip get(long actorId);
	
	/**
	 * 更新
	 * @param actorId
	 * @return
	 */
	public boolean updata(Vip vip);
	
	
	
}
