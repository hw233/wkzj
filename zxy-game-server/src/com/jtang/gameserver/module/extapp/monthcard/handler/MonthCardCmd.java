package com.jtang.gameserver.module.extapp.monthcard.handler;

public interface MonthCardCmd {

	
	/**
	 * 获取月卡信息
	 * 请求:{@code Request}
	 * 返回:{@code MonthCardResponse}
	 * 推送:{@code MonthCardResponse}
	 */
	byte GET_MONTH_CARD_INFO = 1;
	
	/**
	 * 领取今天点券
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 */
	byte GET_MONTH_CARD_REWARD = 2;
	
	/**
	 * 领取终身卡奖励
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 */
	byte GET_LIFELONG_REWARD = 3;
	
	/**
	 * 领取年卡奖励
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 */
	byte GET_YEAR_REWARD = 4;
}
