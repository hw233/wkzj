package com.jtang.gameserver.admin.facade;

import com.jtang.core.result.Result;

public interface GoodsMaintianFacade {

	/**
	 *  添加物品
	 * @param uid
	 * @param serverId
	 * @param platformId
	 * @param goodsId
	 * @param num
	 * @return
	 */
	public abstract Result addGoods(long actorId, int goodsId, int num);

	/**
	 * 删除物品
	 * @param actorId
	 * @param goodsId
	 * @param num
	 * @return
	 */
	public abstract Result deleteGoods(long actorId, int goodsId, int num);

	/**
	 *  添加所有物品
	 * @param actorId
	 * @return
	 */
	public abstract Result addAllGoods(long actorId);
}