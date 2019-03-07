package com.jtang.gameserver.module.extapp.invite.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.invite.facade.InviteFacade;
import com.jtang.gameserver.module.extapp.invite.handler.request.InviteRewardRequest;
import com.jtang.gameserver.module.extapp.invite.handler.request.TargetInviteRequest;
import com.jtang.gameserver.module.extapp.invite.handler.response.InviteResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class InviteHandler extends GatewayRouterHandlerImpl {

	@Autowired
	InviteFacade inviteFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.INVITE;
	}
	
	@Cmd(Id = InviteCmd.INVITE_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<InviteResponse> result = inviteFacade.getInfo(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			sessionWrite(session, response, result.item);
		}
	}
	
	@Cmd(Id = InviteCmd.TARGET_INVITE)
	public void acceptInvitation(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TargetInviteRequest request = new TargetInviteRequest(bytes);
		Result result = inviteFacade.acceptInvitation(actorId, request.inviteCode);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = InviteCmd.GET_REWARD)
	public void getReward(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		InviteRewardRequest request = new InviteRewardRequest(bytes);
		Result result = inviteFacade.getInviteReward(actorId, request.inviteLevel);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = InviteCmd.RESET_INVITE)
	public void resetBeInviter(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = inviteFacade.resetBeInviter(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
}
