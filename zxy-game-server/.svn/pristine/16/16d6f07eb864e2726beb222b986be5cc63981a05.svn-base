package com.jtang.gameserver.module.extapp.welkin.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.welkin.handler.response.WelkinRankResponse;
import com.jtang.gameserver.module.extapp.welkin.handler.response.WelkinResponse;

public interface WelkinFacade {

	/**
	 * 获取天宫寻宝信息
	 */
	public TResult<WelkinResponse> getWelkinInfo(long actorId);

	/**
	 * 抽奖
	 * @param actorId
	 * @param count
	 * @return
	 */
	public TResult<WelkinResponse> welkin(long actorId, int count);

	/**
	 * 获取排行
	 * @return
	 */
	public TResult<WelkinRankResponse> getRank();

}
