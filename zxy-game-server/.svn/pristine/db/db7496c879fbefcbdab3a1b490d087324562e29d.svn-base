package com.jtang.gameserver.admin.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.SysmailMaintianFacade;
import com.jtang.gameserver.admin.handler.request.SendSysmailRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

@Component
public class SysmailMaintianHandler extends AdminRouterHandlerImpl{

	@Autowired
	SysmailMaintianFacade sysmailMaintianFacade;
	
	@Override
	public byte getModule() {
		return GameAdminModule.SYSMAIL;
	}
	
	@Cmd(Id = SysmailMaintianCmd.SEND_SYSMAIL)
	public void sendSysmail(IoSession session, byte[] bytes, Response response) {
		SendSysmailRequest request = new SendSysmailRequest(bytes);
		Result result = sysmailMaintianFacade.sendSysMail(request.actorId,request.list,request.text);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

}
