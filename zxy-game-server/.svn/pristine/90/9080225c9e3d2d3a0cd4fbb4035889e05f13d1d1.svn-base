package com.jtang.gameserver.admin.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.EnhancedMaintianFacade;
import com.jtang.gameserver.admin.handler.request.ModifyEnhancedRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

@Component
public class EnhancedMaintianHandler extends AdminRouterHandlerImpl {

	@Autowired
	EnhancedMaintianFacade enhancedMaintianFacade;
	
	@Override
	public byte getModule() {
		return GameAdminModule.ENHANCED;
	}
	
	@Cmd(Id=EnhancedMaintianCmd.MODIFY_ENHANCED)
	public void modifyEnhanced(IoSession session, byte[] bytes, Response response){
		ModifyEnhancedRequest enhancedRequest = new ModifyEnhancedRequest(bytes);
		Result result=enhancedMaintianFacade.modifyEnhanced(enhancedRequest.actorId,enhancedRequest.targetLevel);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

}
