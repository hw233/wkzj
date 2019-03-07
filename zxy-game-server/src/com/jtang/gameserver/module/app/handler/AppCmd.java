package com.jtang.gameserver.module.app.handler;

/**
 * 活动应用命令接口
 * @author 0x737263
 *
 */
public interface AppCmd {
	
	/**
	 * <pre>
	 * 请求：{@code Request}
	 * 响应：{@code GetAppGlobalResponse}
	 * 推送：{@code GetAppGlobalResponse}
	 * </pre>
	 */
	byte GET_APP_GLOBAL = 1;
	
	/**
	 * 请求:{@code GetAppRecordRequest}
	 * 响应:{@code GetAppRecordResponse}
	 * 推送:{@code GetAppRecordResponse}
	 */
	byte PUSH_APP_RECORD = 2;
	
	/**
	 * <pre>
	 * 请求：{@code GetRewardRequest}
	 * 响应：{@code GetRewardResponse}
	 * </pre>
	 */
	byte GET_REWARD = 3;

}
