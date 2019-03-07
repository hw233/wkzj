package com.jtang.gameserver.admin.handler;


/**
 * 充值命令
 * @author ludd
 *
 */
public interface RechargeCmd {

	/**
	 * 充值
	 * 请求：{@code RechargeRequest }
	 * 响应：{@code Response} {@code AdminStatusCodeConstant}
	 */
	byte RECHARGE = 1;
	/**
	 * 平台赠送点券
	 * 请求：{@code GiveTicketRequest}}
	 * 响应：{@code Response} {@code AdminStatusCodeConstant}
	 */
	byte GIVE = 2;
	
	/**
	 * 管理平台扣除点券
	 * 请求:{@code DecreaseTicketRequest}
	 * 响应:{@code Response}
	 */
	byte DECREASE_TICKET = 3;
	
}
