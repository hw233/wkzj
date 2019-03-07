package com.jtang.gameserver.module.extapp.beast.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.beast.handler.response.BeastInfoResponse;
import com.jtang.gameserver.module.extapp.beast.handler.response.BeastStatusResponse;

public interface BeastFacade {

	
	/**
	 * BeastStatusResponse
	 * @return
	 */
	TResult<BeastStatusResponse> getStatus(long actorId);
	
	/**
	 * 
	 */
	TResult<BeastInfoResponse> getInfo(long actorId);
	
	/**
	 * boss血量百分比
	 * @return
	 */
	TResult<Byte> getBloodPrecent();
	
	/**
	 * 攻击...
	 * @param actorId
	 * @return
	 */
	Result attack(long actorId, boolean useTicket);
	
}
