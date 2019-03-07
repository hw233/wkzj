package com.jtang.gameserver.admin.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.MsgMaintianFacade;
import com.jtang.gameserver.admin.handler.request.DeleteMsgRequest;
import com.jtang.gameserver.admin.handler.request.SendMsgRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

@Component
public class MsgMaintianHandler extends AdminRouterHandlerImpl {
	
	@Autowired
	MsgMaintianFacade msgMaintianFacade;
	
	
	@Cmd(Id = MsgMaintianCmd.DELETE_MSG)
	public void deleteMsg(IoSession session, byte[] bytes, Response response) {
		DeleteMsgRequest dmr = new DeleteMsgRequest(bytes);
		Result result = msgMaintianFacade.deleteMsg(dmr.actorId,dmr.msgId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = MsgMaintianCmd.SEND_MSG)
	public void sendMsg(IoSession session, byte[] bytes, Response response) {
		SendMsgRequest smr = new SendMsgRequest(bytes);
		Result result = msgMaintianFacade.sendMsg(smr.actorId,smr.toActorId,smr.content);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

	@Override
	public byte getModule() {
		return GameAdminModule.MSG;
	}
	
}
