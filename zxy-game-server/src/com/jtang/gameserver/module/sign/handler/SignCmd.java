package com.jtang.gameserver.module.sign.handler;

public interface SignCmd {

	/**
	 * 获取签到信息
	 * 请求:{@code Request}
	 * 推送:{@code SignInfoResponse}
	 * 返回:{@code SignInfoResponse}
	 */
	byte SIGN_INFO = 1;
	
	/**
	 * 普通签到
	 * 请求:{@code Request}
	 * 返回:{@code SignInfoResponse}
	 */
	byte SIGN = 2;
	
	/**
	 * vip签到
	 * 请求:{@code Request}
	 * 返回:{@code SignInfoResponse}
	 */
	byte VIP_SIGN = 3;
}
