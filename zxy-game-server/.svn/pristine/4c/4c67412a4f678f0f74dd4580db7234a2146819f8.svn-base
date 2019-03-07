package com.jtang.gameserver.module.extapp.invite.handler;

public interface InviteCmd {

	/**
	 * 获取邀请信息
	 * 请求:{@code Request}
	 * 推送:{@code InviteResponse}
	 */
	byte INVITE_INFO = 1;
	
	/**
	 * 接受玩家邀请
	 * 请求:{@code TargetInviteRequest}
	 * 返回:{@code Response}
	 */
	byte TARGET_INVITE = 2;
	
	/**
	 * 领取奖励
	 * 请求:{@code InviteRewardRequest}
	 * 返回:{@code Response}
	 */
	byte GET_REWARD = 3;
	
	/**
	 * 推送奖励变更
	 * 推送:{@code InviteResponse}
	 */
	byte PUSH_REWARD = 4;
	
	/**
	 * 重置被邀请人
	 * 请求:{@code Request}.
	 * 返回:{@code Response}
	 */
	byte RESET_INVITE = 5;
}
