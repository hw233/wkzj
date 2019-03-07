package com.jtang.gameserver.admin.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.VampiirMaintianFacade;
import com.jtang.gameserver.admin.handler.request.ModifyVampiirRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

@Component
public class VampiirMaintianHandler extends AdminRouterHandlerImpl {

	protected final Log LOGGER = LogFactory.getLog(VampiirMaintianHandler.class);
	
	@Autowired
	VampiirMaintianFacade vamiirMaintianFacade;
	
	@Override
	public byte getModule() {
		return GameAdminModule.VAMPIIR;
	}
	
	@Cmd(Id = VampiirMaintianCmd.MODIFY_VAMPIIR)
	public void modifyVampiir(IoSession session, byte[] bytes, Response response){
		ModifyVampiirRequest modifyVaRequest = new ModifyVampiirRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+ modifyVaRequest.actorId+" ----- targetLevel = " + modifyVaRequest.targetLevel);
		}
		Result result=vamiirMaintianFacade.modifyVampiirLevel(modifyVaRequest.actorId, modifyVaRequest.targetLevel);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

}
