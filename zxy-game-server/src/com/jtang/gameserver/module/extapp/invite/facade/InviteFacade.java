package com.jtang.gameserver.module.extapp.invite.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.invite.handler.response.InviteResponse;

public interface InviteFacade {

	/**
	 * 获取邀请信息
	 * @param actorId
	 * @return
	 */
	TResult<InviteResponse> getInfo(long actorId);
	
	/**
	 * 接受别人邀请
	 * @param beInviteId 被邀请者ID
	 * @param inviteCode 邀请码
	 * @return
	 */
	Result acceptInvitation(long beInviteId, String inviteCode);
	
	/**
	 * 领取奖励
	 * @param actorId
	 * @param inviteLevel 邀请人的等级
	 * @return
	 */
	Result getInviteReward(long actorId, int inviteLevel);
	
	/**
	 * 重置被邀请人
	 * @param actorId
	 * @return
	 */
	Result resetBeInviter(long actorId);
}
