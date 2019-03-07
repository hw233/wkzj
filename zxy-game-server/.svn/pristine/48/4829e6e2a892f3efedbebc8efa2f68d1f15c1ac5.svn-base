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
import com.jtang.gameserver.admin.facade.ReciruitMaintianFacade;
import com.jtang.gameserver.admin.handler.request.ModifyRecruitLevelRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

@Component
public class ReciruitMaintianHandler extends AdminRouterHandlerImpl {

	protected final Log LOGGER = LogFactory.getLog(ReciruitMaintianHandler.class);
	
	@Autowired
	ReciruitMaintianFacade reciruitMaintianFacade;

	@Override
	public byte getModule() {
		return GameAdminModule.RECIRUIT;
	}

	@Cmd(Id = ReciruitMaintianCmd.MODIFY_RECIRUIT)
	public void modifyReciruit(IoSession session, byte[] bytes, Response response) {
		ModifyRecruitLevelRequest modifyRecruit = new ModifyRecruitLevelRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+ modifyRecruit.actorId+" ----- targetLevel = " + modifyRecruit.targetLevel);
		}
		Result result = reciruitMaintianFacade.modifyReciruitLevel(modifyRecruit.actorId, modifyRecruit.targetLevel);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

}
