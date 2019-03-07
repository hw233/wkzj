package com.jtang.gameserver.module.adventures.vipactivity.facade;

import com.jtang.core.result.Result;

public interface ResetHeroEquipFacade {

	/**
	 * 重置装备
	 * @param actorId
	 * @param uuid
	 * @return
	 */
	public Result resetEquip(long actorId, long uuid);

	/**
	 * 重置装备
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	public Result resetHero(long actorId, int heroId);

}
