package com.jtang.worldserver.module.base.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jiatang.common.baseworld.BaseWorldCmd;
import com.jiatang.common.baseworld.request.SessionRegisterG2W;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.ActorRequest;
import com.jtang.core.protocol.ActorResponse;
import com.jtang.core.result.Result;
import com.jtang.core.utility.TimeUtils;
import com.jtang.worldserver.module.base.facade.BaseFacade;
import com.jtang.worldserver.module.base.handler.request.ReloadConfigRequest;
import com.jtang.worldserver.server.router.WorldRouterHandlerImpl;
import com.jtang.worldserver.server.session.WorldSession;

@Component
public class BaseWorldHandler extends WorldRouterHandlerImpl {

	@Autowired
	WorldSession worldSession;
	
	@Autowired
	BaseFacade baseFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.BASE;
	}

	@Cmd(Id = BaseWorldCmd.HEART_BEAT)
	public void heartBeat(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
		response.setValue(TimeUtils.getNowBytes());
		sessionWrite(session, response);
	}
	
	@Cmd(Id = BaseWorldCmd.REGISTER)
	public void register(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
		SessionRegisterG2W registerG2W = new SessionRegisterG2W(request.getValue());
		worldSession.register(registerG2W.serverId, session);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = BaseWorldCmd.SHTUDOWN)
	public void shtudown(IoSession session, int serverId, ActorRequest request, ActorResponse response){
		Result result = baseFacade.shutdownServer();
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = BaseWorldCmd.RELOAD_CONFIG)
	public void reloadConfig(IoSession session, int serverId, ActorRequest request, ActorResponse response){
		ReloadConfigRequest reloadConfigRequest = new ReloadConfigRequest(request.getValue());
		Result result = baseFacade.reloadConfig(reloadConfigRequest.fileName,reloadConfigRequest.data);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}

}
