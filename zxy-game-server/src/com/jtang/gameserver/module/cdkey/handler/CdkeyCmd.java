package com.jtang.gameserver.module.cdkey.handler;

public interface CdkeyCmd {

	/**
	 * 获取礼包 
	 * 请求:{@code CdkeyRequest}
	 * 返回:{@code CdkeyResponse}
	 */
	byte GET_PACKAGE = 1;
}
