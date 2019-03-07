package com.jtang.gameserver.module.trialcave.dao;

import com.jtang.gameserver.dbproxy.entity.TrialCave;

/**
 * 试炼洞DAO
 * @author lig
 *
 */
public interface TrialCaveDao {
	/**
	 * 获取试炼洞信息
	 * @param actorId
	 * @return
	 */
	public TrialCave get(long actorId);
	
	/**
	 * 更新试炼洞信息
	 * @param cave
	 */
	public void update(TrialCave cave);
}
