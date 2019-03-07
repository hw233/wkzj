package com.jtang.gameserver.module.adventures.favor.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.favor.model.FavorVO;

public interface FavorFacade {
	/**
	 * 获取福神眷顾
	 * @param actorId
	 * @return
	 */
	TResult<FavorVO> get(long actorId);
	
	/**
	 * 使用福神眷顾中的特权
	 * @param actorId
	 * @param privilegeId
	 * @return
	 */
	TResult<FavorVO> usePrivilege(long actorId, int privilegeId);
	
}
