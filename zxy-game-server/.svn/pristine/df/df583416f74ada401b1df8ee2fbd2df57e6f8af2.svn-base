package com.jtang.gameserver.module.adventures.vipactivity.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.GiveEquipInfoResponse;

public interface GiveEquipFacade {

	/**
	 * 赠送盟友宝物
	 * @param actorId
	 * @param otherId 
	 * @return
	 */
	TResult<Integer> giveEquip(long actorId, long otherId);

	/**
	 * 请求天财地宝信息
	 * @param actorId
	 * @return
	 */
	TResult<GiveEquipInfoResponse> giveEquipInfo(long actorId);

}
