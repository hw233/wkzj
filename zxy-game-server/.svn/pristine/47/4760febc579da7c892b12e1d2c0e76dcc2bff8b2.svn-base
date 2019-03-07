package com.jtang.gameserver.module.extapp.onlinegifts.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.onlinegifts.handler.response.OnlineGiftsInfoResponse;

public interface OnlineGiftsFacade {
	
	/**
	 * 获取在线礼包信息
	 * @param actorId
	 * @return
	 */
	TResult<OnlineGiftsInfoResponse> getOnlineInfo(long actorId);
	
	/**
	 * 领取奖励
	 * @param actorId
	 * @return
	 */
	Result receiveGift(long actorId);

}
