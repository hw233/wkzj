package com.jtang.gameserver.module.delve.dao;

import com.jtang.gameserver.dbproxy.entity.Delve;
/**
 * 潜修室Dao
 * @author ludd
 *
 */
public interface DelveDao {
	/**
	 * 获取潜修室
	 * @param actorId 角色Id
	 * @return 如果查询数据库失败了 会返回null
	 */
	public Delve get(long actorId);

	/**
	 * 更新潜修室等级
	 * @param actorId 角色ID
	 * @param level 等级
	 * @return
	 */
	public boolean update(long actorId, int level);
}
