package com.jtang.gameserver.module.extapp.vipbox.handler;

public interface VipBoxCmd {

	/**
	 * 获取活动信息
	 * 请求:{@code Request}
	 * 返回:{@code VipBoxResponse}
	 */
	byte GET_BOX_INFO = 1;
	
	/**
	 * 获取箱子
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 * 推送:{@code VipBoxResponse}
	 */
	byte GET_BOX = 2;
	
	/**
	 * 推送箱子更新
	 * 推送:{@code VipBoxResponse}
	 */
	byte PUSH_BOX = 3;
	
	/**
	 * 获取活动开启关闭时间
	 * 请求:{@code Request}
	 * 返回:{@code VipBoxConfigResponse}
	 */
	byte GET_CONFIG = 4;
}
