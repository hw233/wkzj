package com.jtang.gameserver.module.extapp.craftsman.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.craftsman.handler.response.BuildEquipResponse;

public interface CraftsmanFacade {

	/**
	 * 打造
	 * @param actorId
	 * @param equipUUID 装备uuid
	 * @param buildStar 放入装备的星级
	 * @param useTicket 是否使用点券
	 * @return
	 */
	TResult<BuildEquipResponse> build(long actorId, long uuid, byte buildStar, boolean useTicket);
	
	/**
	 * 获取用户的打造次数
	 * @param actorId
	 * @return
	 */
	int getBuildNum(long actorId);
}
