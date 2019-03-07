package com.jtang.gameserver.admin.handler;


import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.ChatMaintianFacade;
import com.jtang.gameserver.admin.handler.request.ActorChatForbiddenRequest;
import com.jtang.gameserver.admin.handler.request.ActorChatUnforbiddenRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

/**
 * 聊天禁言
 * @author ligang
 */
@Component
public class ChatMaintianHandler extends AdminRouterHandlerImpl {
	
	@Autowired
	ChatMaintianFacade chatFacade;
	
	@Override
	public byte getModule() {
		return GameAdminModule.CHAT;
	}
	
	@Cmd(Id = ChatMaintianCmd.FORBIDDEN_CHAT)
	public void forbiddenActorChat(IoSession session, byte[] bytes, Response response) {
		ActorChatForbiddenRequest actorChatForbiddenRequest = new ActorChatForbiddenRequest(bytes);
		Result result = chatFacade.sendForbiddenChat(actorChatForbiddenRequest.actorId, actorChatForbiddenRequest.unforbiddenTime);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = ChatMaintianCmd.UNFORBIDDEN_CHAT)
	public void unforbiddenActorChat(IoSession session, byte[] bytes, Response response) {
		ActorChatUnforbiddenRequest actorChatUnforbiddenRequest = new ActorChatUnforbiddenRequest(bytes);
		Result result = chatFacade.sendUnforbiddenChat(actorChatUnforbiddenRequest.actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

}
