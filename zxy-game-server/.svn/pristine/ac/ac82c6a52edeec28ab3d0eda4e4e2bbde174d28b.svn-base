package com.jtang.gameserver.module.extapp.ernie.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.ernie.facade.ErnieFacade;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieInfoResponse;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieRecordResponse;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieStatusResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class ErnieHandler extends GatewayRouterHandlerImpl {

	@Autowired
	ErnieFacade ernieFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.ERNIE;
	}

	@Cmd(Id = ErnieCmd.STATUS)
	public void getStatus(IoSession session, byte[] bytes, Response response){
		TResult<ErnieStatusResponse> result = ernieFacade.getStatus();
		if (result.isOk()) {
			sessionWrite(session, response, result.item);
		} else {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
	

	@Cmd(Id = ErnieCmd.ERNIE_RECORD)
	public void getRecord(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<ErnieRecordResponse> result = ernieFacade.getRecord(actorId);
		if (result.isOk()) {
			sessionWrite(session, response, result.item);
		} else {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
	

	@Cmd(Id = ErnieCmd.ERNIE_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<ErnieInfoResponse> result = ernieFacade.getInfo(actorId);
		if (result.isOk()) {
			sessionWrite(session, response, result.item);
		} else {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
	

	@Cmd(Id = ErnieCmd.RUN_ERNIE)
	public void runErnie(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<ErnieInfoResponse> result = ernieFacade.runErnie(actorId);
		if (result.isOk()) {
			sessionWrite(session, response, result.item);
		} else {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
}
