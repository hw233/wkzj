package com.jtang.gameserver.module.user.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.UserDisabled;

public interface UserDisableFacade {
	/**
	 * 封号
	 * @param time 封号时长(秒)
	 */
	public Result disable(long actorId,int time);

	/**
	 * 解封
	 */
	public Result enable(long actorId);
	
	/**
	 * 是否封号
	 * @param actorId
	 * @param sim
	 * @param mac
	 * @param imei
	 * @param ip
	 */
	public TResult<UserDisabled> isDisable(long actorId, String sim, String mac, String imei, String ip);
}
