package com.jtang.gameserver.module.msg.handler;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Message;
import com.jtang.gameserver.module.msg.facade.MsgFacade;
import com.jtang.gameserver.module.msg.handler.request.RemoveMsgRequest;
import com.jtang.gameserver.module.msg.handler.request.SendMsgRequest;
import com.jtang.gameserver.module.msg.handler.response.MsgListResponse;
import com.jtang.gameserver.module.msg.handler.response.MsgResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class MsgHandler extends GatewayRouterHandlerImpl {

	@Autowired
	private MsgFacade msgFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.MSG;
	}
	
	@Cmd(Id = MsgCmd.GET_MSG_LIST)
	public void get(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		List<Message> msgList = msgFacade.get(actorId);
		MsgListResponse packet = new MsgListResponse(msgList);
		sessionWrite(session, response, packet);
	}
	
	@Cmd(Id = MsgCmd.SEND_MSG)
	public void sendMsg(IoSession session, byte[] bytes, Response response) {
		SendMsgRequest request = new SendMsgRequest(bytes);
		long fromActorId = playerSession.getActorId(session);
		TResult<Message> result = msgFacade.sendMsg(fromActorId, request.toActorId, request.content);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			MsgResponse packet = new MsgResponse(result.item);
			sessionWrite(session, response, packet);
		}
	}
	
	@Cmd(Id = MsgCmd.REMOVE_MSG)
	public void removeMsg(IoSession session, byte[] bytes, Response response){
		RemoveMsgRequest request = new RemoveMsgRequest(bytes);
		long actorId = playerSession.getActorId(session);
		Result result = msgFacade.removeMsg(actorId, request.mIdList);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = MsgCmd.SET_READED)
	public void setReaded(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Result result = msgFacade.setReaded(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
}
