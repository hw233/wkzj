package com.jtang.gameserver.module.extapp.welkin.handler;

public interface WelkinCmd {

	/**
	 * 请求天宫探物信息
	 * 请求:{@code Request}
	 * 返回:{@code WelkinResponse}
	 */
	byte WELKIN_INFO = 1;
	
	/**
	 * 探物
	 * 请求:{@code WelkinRequest}
	 * 返回:{@code WelkinResponse}
	 */
	byte WELKIN = 2;
	
	/**
	 * 获取排行榜
	 * 请求:{@code Request}
	 * 返回:{@code WelkinRankResponse}
	 */
	byte WELKIN_RANK = 3;
	
	/**
	 * 推送天宫探物状态
	 * 推送:{@code WelkinStateResponse}
	 */
	byte PUSH_STATE = 4;
	
	/**
	 * 推送天宫探物信息变更
	 * 推送:{@code WelkinResponse}
	 */
	byte PUSH_WELKIN_INFO = 5;
}
