package com.jtang.gameserver.admin.facade;

import com.jtang.core.result.Result;

public interface ReciruitMaintianFacade {
	
	/**
	 * 修改聚仙阵等级
	 * @param actorId
	 * @param level
	 * @return
	 */
	public Result modifyReciruitLevel(long actorId, int level);
}
