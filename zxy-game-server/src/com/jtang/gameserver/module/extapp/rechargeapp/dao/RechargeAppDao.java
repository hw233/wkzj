package com.jtang.gameserver.module.extapp.rechargeapp.dao;

import com.jtang.gameserver.dbproxy.entity.RechargeApp;

public interface RechargeAppDao {

	/**
	 * 获取充值活动记录
	 */
	public RechargeApp get(long actorId);
	
	/**
	 * 
	 */
	public void update(RechargeApp rechargeApp);
}
