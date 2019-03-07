package com.jtang.gameserver.module.equipdevelop.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.equipdevelop.handler.request.EquipConvertRequest;
import com.jtang.gameserver.module.equipdevelop.handler.response.EquipDevelopResponse;

public interface EquipDevelopFacade {

	/**
	 * 装备、装备碎片提炼
	 * @param actorId
	 * @param request
	 * @return
	 */
	short equipConvert(long actorId,EquipConvertRequest request);
	
	/**
	 * 装备突破
	 * @param actorId
	 * @param uuid
	 * @return
	 */
	TResult<EquipDevelopResponse> equipDevelop(long actorId,long uuid);
}
