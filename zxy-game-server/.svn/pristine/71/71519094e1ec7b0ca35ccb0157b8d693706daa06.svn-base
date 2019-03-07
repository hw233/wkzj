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
import com.jtang.gameserver.admin.facade.AllyMaintianFacade;
import com.jtang.gameserver.admin.handler.request.AddAllayRequest;
import com.jtang.gameserver.admin.handler.request.DeleteAllayRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

@Component
public class AllyMaintianHandler extends AdminRouterHandlerImpl {

	protected final Log LOGGER = LogFactory.getLog(AllyMaintianHandler.class);

	@Autowired
	AllyMaintianFacade allyMaintianFacade;

	@Override
	public byte getModule() {
		return GameAdminModule.ALLY;
	}

	@Cmd(Id = AllyMaintianCmd.ADD_ALLY)
	public void addAlly(IoSession session, byte[] bytes, Response response) {
		AddAllayRequest addAlly = new AddAllayRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = " + addAlly.actorId + "----" + "allyId = " + addAlly.allyId);
		}
		Result result = allyMaintianFacade.addAlly(addAlly.actorId, addAlly.allyId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

	@Cmd(Id = AllyMaintianCmd.DEL_ALLY)
	public void deleteAlly(IoSession session, byte[] bytes, Response response) {
		DeleteAllayRequest delAlly = new DeleteAllayRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = " + delAlly.actorId + "----" + "allyId = " + delAlly.allyId);
		}
		Result result = allyMaintianFacade.deleteAlly(delAlly.actorId, delAlly.allyId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

}
