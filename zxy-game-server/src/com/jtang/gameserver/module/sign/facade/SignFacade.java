package com.jtang.gameserver.module.sign.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.sign.handler.response.SignInfoResponse;


public interface SignFacade {

	/**
	 * 获取签到信息
	 * @param actorId
	 * @return
	 */
	TResult<SignInfoResponse> getInfo(long actorId);

	/**
	 * 签到
	 * @param actorId
	 * @return
	 */
	TResult<SignInfoResponse> sign(long actorId);

	/**
	 * vip签到
	 * @param actorId
	 * @return
	 */
	TResult<SignInfoResponse> vipSign(long actorId);


}
