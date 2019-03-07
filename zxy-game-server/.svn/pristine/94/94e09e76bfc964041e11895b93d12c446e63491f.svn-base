package com.jtang.gameserver.module.love.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.love.handler.response.GetMarryGiftResponse;
import com.jtang.gameserver.module.love.handler.response.LoveInfoResponse;

public interface LoveFacade {
	/**
	 * 获取婚姻信息
	 * @param actorId
	 * @return
	 */
	LoveInfoResponse getLoveInfo(long actorId);

	/**
	 * 请求结婚
	 * @param actorId
	 * @param targetActorId
	 * @return
	 */
	Result marry(long actorId, long targetActorId);

	/**
	 * 接受或拒绝结婚
	 * @param actorId
	 * @param accept
	 * @param targetActorId
	 * @return
	 */
	TResult<LoveInfoResponse> acceptMarry(long actorId, byte accept, long targetActorId);

	/**
	 * 离婚
	 * @param actorId
	 * @return
	 */
	TResult<LoveInfoResponse> unMarry(long actorId);

	/**
	 * 送礼
	 * @param actorId
	 * @return
	 */
	Result giveGift(long actorId);

	/**
	 * 收礼
	 * @param actorId
	 * @return
	 */
	TResult<GetMarryGiftResponse> acceptGift(long actorId);

	/**
	 * 是否已结婚
	 * @param actorId
	 * @return
	 */
	boolean isMarry(long actorId);

}
