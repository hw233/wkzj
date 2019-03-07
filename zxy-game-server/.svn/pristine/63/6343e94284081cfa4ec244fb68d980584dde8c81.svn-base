package com.jtang.gameserver.admin.facade;

import java.math.BigDecimal;

import com.jtang.core.result.Result;

public interface RechargeFacade {

	/**
	 * 充值点券
	 * @param orderSnid
	 * @param platformId
	 * @param serverId
	 * @param uid
	 * @param payWayId
	 * @param tradeSnid
	 * @param rechargeId
	 * @param discount
	 * @param payMoney
	 * @param rechargeTicketNum
	 * @param giveTicketNum
	 * @param mediaId
	 * @param channelId
	 * @param reachargeTime
	 * @param actorId
	 * @return
	 */
	public Result recharge(String orderSnid, int platformId, int serverId, String uid, int payWayId, String tradeSnid, int rechargeId, byte discount,
			BigDecimal payMoney, int rechargeTicketNum, int giveTicketNum, int mediaId, int channelId, int reachargeTime,long actorId);

	/**
	 * 平台赠送点券
	 * @param actorId
	 * @param giveNum
	 * @return
	 */
	public Result giveTicket(long actorId, int giveNum);

	/**
	 * 管理平台扣除点券
	 * @param actorId
	 * @param ticket
	 * @return
	 */
	public Result dicreaseTicket(long actorId, int ticket);
	
	
	
}
