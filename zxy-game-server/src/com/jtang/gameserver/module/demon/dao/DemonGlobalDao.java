package com.jtang.gameserver.module.demon.dao;

import com.jtang.gameserver.dbproxy.entity.DemonGlobal;
/**
 * 集众降魔boss dao
 * @author ludd
 *
 */
public interface DemonGlobalDao {
	/**
	 * 获取
	 * @param difficult 难度
	 * @return
	 */
	DemonGlobal get(long difficult);
	/**
	 * 更新
	 * @param demonGloabal
	 * @return
	 */
	boolean update(DemonGlobal demonGloabal);
}
