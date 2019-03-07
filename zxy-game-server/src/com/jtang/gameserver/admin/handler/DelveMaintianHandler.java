package com.jtang.gameserver.admin.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.DelveMaintianFacade;
import com.jtang.gameserver.admin.handler.request.ModifyDelveLevelRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

@Component
public class DelveMaintianHandler extends AdminRouterHandlerImpl {

	@Autowired
	DelveMaintianFacade delveMaintianFacade;

	@Override
	public byte getModule() {
		return GameAdminModule.DELVE;
	}

	@Cmd(Id = DelveMaintianCmd.MODIFY_DELVE_LEVEL)
	public void modifyDelveLevel(IoSession session, byte[] bytes, Response response) {
		ModifyDelveLevelRequest modifyDelveRequest = new ModifyDelveLevelRequest(bytes);
		long actorId = modifyDelveRequest.actorId;
		Result result = delveMaintianFacade.modifyDelveLevel(actorId, modifyDelveRequest.targetLevel);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

}
