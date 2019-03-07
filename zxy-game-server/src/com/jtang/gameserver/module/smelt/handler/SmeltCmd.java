package com.jtang.gameserver.module.smelt.handler;

public interface SmeltCmd {

	/**
	 * 请求熔炉转换
	 * {@code SmeltRequest}
	 * {@code SmeltResponse}
	 */
	byte SMELT_CONVERT = 1;
	
	/**
	 * 请求商店兑换信息
	 * {@code Request}
	 * {@code SmeltInfoResponse}
	 */
	byte SMELT_EXCHANGE_INFO = 2;
	
	/**
	 * 请求商店兑换
	 * {@code SmeltExchangeRequest}
	 * {@code response}
	 */
	byte SMELT_EXCHANGE = 3;
}
