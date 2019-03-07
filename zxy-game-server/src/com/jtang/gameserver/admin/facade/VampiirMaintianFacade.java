package com.jtang.gameserver.admin.facade;

import com.jtang.core.result.Result;

public interface VampiirMaintianFacade {
	
	/**
	 * 修改吸灵室等级
	 * @param actorId
	 * @param level
	 * @return
	 */
	public Result modifyVampiirLevel(long actorId, int level);
}
