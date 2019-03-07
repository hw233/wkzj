package com.jtang.gameserver.admin.facade;

import com.jtang.core.result.Result;

public interface AllyMaintianFacade {

	/**
	 * 删除盟友
	 * @param actorId
	 * @param allyId
	 * @return
	 */
	public abstract Result deleteAlly(long actorId, long allyId);

	/**
	 * 增加盟友
	 * @param actorId
	 * @param allyId
	 * @return
	 */
	public abstract Result addAlly(long actorId, long allyId);

}