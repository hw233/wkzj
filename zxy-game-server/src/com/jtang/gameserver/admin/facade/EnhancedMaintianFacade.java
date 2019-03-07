package com.jtang.gameserver.admin.facade;

import com.jtang.core.result.Result;

public interface EnhancedMaintianFacade {

	/**
	 * 设置强化室等级
	 * @param actorId
	 * @param targetLevel
	 */
	Result modifyEnhanced(long actorId, int targetLevel);

}
