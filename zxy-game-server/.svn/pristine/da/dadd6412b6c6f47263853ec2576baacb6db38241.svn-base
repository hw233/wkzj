package com.jtang.gameserver.module.sysmail.handler;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Sysmail;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.sysmail.handler.request.SysmailRequest;
import com.jtang.gameserver.module.sysmail.handler.response.SysmailListResponse;
import com.jtang.gameserver.module.sysmail.handler.response.SysmailResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

/**
 * 系统邮件逻辑处理句柄
 * 
 * @author 0x737263
 * 
 */
@Component
public class SysmailHandler extends GatewayRouterHandlerImpl {

	@Autowired
	SysmailFacade sysmailFacade;

	@Override
	public byte getModule() {
		return ModuleName.SYSMAIL;
	}

	@Cmd(Id = SysmailCmd.GET_SYSMAIL_LIST)
	public void getList(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		List<Sysmail> list = sysmailFacade.getList(actorId);

		SysmailListResponse packet = new SysmailListResponse(list);
		sessionWrite(session, response, packet);
	}

	@Cmd(Id = SysmailCmd.GET_ATTACH)
	public void getAttach(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		SysmailRequest request = new SysmailRequest(bytes);

		TResult<Sysmail> result = sysmailFacade.getAttach(actorId, request.sysmailId);
		if (result.isOk()) {
			SysmailResponse sysmailResponse = new SysmailResponse(result.item);
			response.setValue(sysmailResponse.getBytes());
		}
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}

	@Cmd(Id = SysmailCmd.REMOVE_SYSMAIL)
	public void removeSysmail(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		SysmailRequest request = new SysmailRequest(bytes);

		Result result = sysmailFacade.remove(actorId, request.sysmailId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = SysmailCmd.ONE_KEY_GET_ATTACH)
	public void oneKeyGetAttach(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Result result = sysmailFacade.oneKeyGetAttach(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

}
