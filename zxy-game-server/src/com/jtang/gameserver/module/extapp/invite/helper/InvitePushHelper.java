package com.jtang.gameserver.module.extapp.invite.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.dbproxy.entity.Invite;
import com.jtang.gameserver.module.extapp.invite.handler.InviteCmd;
import com.jtang.gameserver.module.extapp.invite.handler.response.InviteResponse;
import com.jtang.gameserver.module.extapp.invite.type.ReceiveStatusType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class InvitePushHelper {
	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<InvitePushHelper> ref = new ObjectReference<InvitePushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static InvitePushHelper getInstance() {
		return ref.get();
	}
	
	
	/**
	 * 
	 * @param invite 邀请着
	 * @param inviteName 被邀请者名字
	 */
	public static void pushInviteReward(Invite invite, String inviteName){
		InviteResponse response = new InviteResponse();
		response.inviteCode = invite.inviteCode;
		response.inviteName = inviteName;
		response.isInvite = invite.targetInvite == 0L? ReceiveStatusType.DID_NOT_RECEIVE.getType() : ReceiveStatusType.CAN_RECEIVE.getType();
		response.rewardMap = invite.rewardMap;
		Response rsp = Response.valueOf(ModuleName.INVITE, InviteCmd.PUSH_REWARD, response.getBytes());
		
		getInstance().playerSession.push(invite.actorId, rsp);
	}
}
