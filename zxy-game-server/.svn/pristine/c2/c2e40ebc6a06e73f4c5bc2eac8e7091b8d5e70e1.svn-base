package com.jtang.gameserver.module.chat.handler;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.module.chat.facade.ChatFacade;
import com.jtang.gameserver.module.chat.handler.request.ChatRequest;
import com.jtang.gameserver.module.chat.handler.response.ChatResponse;
import com.jtang.gameserver.module.chat.handler.response.HistoryChatResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class ChatHandler extends GatewayRouterHandlerImpl{

	@Autowired
	private ChatFacade chatFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.CHAT;
	}
	
	@Cmd(Id = ChatCmd.WORLD_CHAT)
	public void chat(IoSession session, byte[] bytes, Response response) {
		ChatRequest chatRequest = new ChatRequest(bytes);
		long actorId = playerSession.getActorId(session);
		Result result = chatFacade.chat(actorId, chatRequest.msg);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = ChatCmd.HISTORY_CHAT)
	public void broadcastHistoryChat(IoSession session,byte[] bytes,Response response){
		List<ChatResponse> historyMsg = chatFacade.getChat();
		HistoryChatResponse histChatResponse=new HistoryChatResponse(historyMsg);
		sessionWrite(session, response,histChatResponse);
	}
	
	
}
