package com.jtang.gameserver.module.adventures.vipactivity.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.MainHeroResponse;

public interface MainHeroFacade {
	
	/**
	 * 设置主力仙人
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	TResult<MainHeroResponse> setMainHero(long actorId, int heroId, boolean useGold);

	/**
	 * 获取主力仙人信息
	 * @param actorId
	 * @return
	 */
	TResult<MainHeroResponse> getMainHeroInfo(Long actorId);
	
	/**
	 * 判断是否是主力仙人
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	public boolean isMainHero(long actorId, int heroId);
	
	/**
	 * 重置主力仙人
	 * @param actorId
	 * @param isPush 是否推送客户端
	 */
	public void resetMainHero(long actorId, boolean isPush);
}
