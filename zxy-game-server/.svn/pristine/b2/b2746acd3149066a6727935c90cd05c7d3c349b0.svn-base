package com.jtang.gameserver.admin.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.NotifyMaintianFacade;
import com.jtang.gameserver.admin.handler.request.DeleteNotifyRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

@Component
public class NotifyMaintianHandler extends AdminRouterHandlerImpl {

	@Autowired
	NotifyMaintianFacade notifyMaintianFacade;
	
	@Cmd(Id = NotifyMaintianCmd.DELETE_NOTICE)
	public void deleteMsg(IoSession session, byte[] bytes, Response response) {
		DeleteNotifyRequest request = new DeleteNotifyRequest(bytes);
		Result result = notifyMaintianFacade.deleteNotify(request.actorId,request.notifyId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	
	@Override
	public byte getModule() {
		return GameAdminModule.NOTIFY;
	}

}
