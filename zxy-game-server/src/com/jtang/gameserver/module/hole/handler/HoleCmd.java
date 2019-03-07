package com.jtang.gameserver.module.hole.handler;

/**
 * 洞府模块命令接口
 * */
public interface HoleCmd {
	/**
	 * 获取洞府信息
	 * 请求:{@code Request}
	 * 响应:{@code HoleResponse}
	 * */
	byte GET_HOLE_INFO = 1;
	
	/**
	 * 获取通关奖励
	 * 请求:{@code HoleRewardRequest}
	 * 响应:{@code Response}
	 * */
	byte GET_HOLE_REWARD = 2;
	
	/**
	 * 战斗
	 * 请求:{@code HoleFightRequest}
	 * 推送:{@code HoleFightResponse}
	 * */
	byte HOLE_FIGHT = 3;
	
//	/**
//	 * 上古洞府消失
//	 * 推送:{@code HoleCloseResponse}
//	 */
//	byte PUSH_HOLE_DISAPPER = 4;
	
	/**
	 * 开启上古洞府
	 * 推送:{@code HoleOpenResponse}
	 */
	byte PUSH_HOLE_OPEN = 5;
	
	/**
	 * 通知盟友
	 * 请求:{@code HoleRequest}
	 * 响应:{@code Response}
	 */
	byte SEND_ALLY = 6;
	
	/**
	 * 推送盟友洞府
	 * 推送:{@code AllyHoleNotifyResponse}
	 */
	byte PUSH_ALLY_HOLE = 7;
	
	/**
	 * 领取盟友通关大礼包
	 * 请求:{@code HoleRequest}
	 * 响应:{@code Response}
	 */
	byte GET_HOLE_PACKAGE_GIFT = 8;
	
	/**
	 * 推送盟友通关数量
	 * 推送:{@code AllyFightNumResponse}
	 */
	byte PUSH_ALLY_NUM = 9;
	
	/**
	 * 受邀请盟友开启洞府
	 * 请求:{@code HoleAllyOpenRequest}
	 * 响应:{@code Response}
	 * 推送:{@code HoleOpenResponse}
	 */
	byte ALLY_OPEN_HOLE = 10;
	
	/**
	 * 推送跨天
	 * 推送:{@code Response}
	 */
	byte PUSH_ZERO = 11;
}
