package com.jtang.gameserver.module.user.dao;

import com.jtang.gameserver.dbproxy.entity.RechargeLog;

public interface RechargeLogDao {
	/**
	 * 保存
	 * @param rechargeLog
	 * @return 1：订单号重复，2数据库错误，0成功
	 */
	public int save(RechargeLog rechargeLog);
	/**
	 * 根据订单号查询记录
	 * @param orderSid
	 * @return
	 */
	public RechargeLog getByOrderSid(String orderSid);
	
	/**
	 * 获得一个时间段内充值金额
	 * @param start
	 * @param end
	 * @return
	 */
	public int getPaymoneyTotal(int platformId, int serverId, String uid, int start, int end);
	
}
