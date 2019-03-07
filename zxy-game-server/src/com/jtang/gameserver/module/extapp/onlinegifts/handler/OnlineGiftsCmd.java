package com.jtang.gameserver.module.extapp.onlinegifts.handler;

public interface OnlineGiftsCmd {

	
	/*
	 * 获取在线礼包信息
	 * 请求:{@code Request}
	 * 返回:{@code OnlineGiftsInfoResponse}
	 * 推送:{@code OnlineGiftsInfoResponse}
	 */
	byte ONLINE_INFO = 1;
	
	
	/**
	 * 领取在线礼包
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 */
	byte RECEIVE = 2;
}
