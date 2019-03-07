package com.jtang.gameserver.admin.facade;

import com.jtang.core.result.Result;

public interface HeroMaintianFacade {

	/**
	 * 删除仙人（先踢下线，禁止登录，删除，开放登录）
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	Result deleteHero(long actorId, int heroId);

	/**
	 * 增加仙人经验
	 * @param actorId
	 * @param heroId
	 * @param exp 增加经验值
	 * @return
	 */
	Result addHeroExp(long actorId, int heroId, int exp);

	/**
	 * 增加仙人魂魄
	 * @param actorId
	 * @param heroId
	 * @param num 数量
	 * @return
	 */
	Result addHeroSoul(long actorId, int heroId, int num);

	/**
	 * 删除仙人魂魄
	 * @param actorId
	 * @param heroId
	 * @param num 数量
	 * @return
	 */
	Result deleteHeroSoul(long actorId, int heroId, int num);
	
	/**
	 * 添加仙人魂魄
	 * @param actorId
	 * @param heroId
	 * @param num 数量
	 * @return
	 */
	Result addAllHeroSoul(long actorId);

}
