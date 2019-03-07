package com.jtang.gameserver.module.ally.dao;

import com.jtang.gameserver.dbproxy.entity.Ally;

public interface AllyDao {

	/**
	 * 返回角色所有的盟友
	 * @param actorId
	 * @return
	 */
	public Ally get(long actorId);
	/**
	 * 持久化盟友到数据库,如果添加/删除/修改了盟友的属性需调用
	 * @param ally
	 * @return
	 */
	public boolean update(Ally ally);
}
