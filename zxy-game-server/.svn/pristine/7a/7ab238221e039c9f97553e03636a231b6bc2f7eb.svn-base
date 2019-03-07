package com.jtang.gameserver.module.extapp.vipbox.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.vipbox.facade.VipBoxFacade;
import com.jtang.gameserver.module.extapp.vipbox.handler.response.VipBoxConfigResponse;
import com.jtang.gameserver.module.extapp.vipbox.handler.response.VipBoxResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class VipBoxHandler extends GatewayRouterHandlerImpl {

	@Autowired
	private VipBoxFacade vipBoxFacade;
	
	@Autowired
	PlayerSession playerSession;
	
	@Override
	public byte getModule() {
		return ModuleName.VIP_BOX;
	}
	
	@Cmd(Id=VipBoxCmd.GET_BOX_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<VipBoxResponse> result = vipBoxFacade.getInfo(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id=VipBoxCmd.GET_BOX)
	public void getBox(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = vipBoxFacade.getBox(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = VipBoxCmd.GET_CONFIG)
	public void getConfig(IoSession session, byte[] bytes, Response response) {
		TResult<VipBoxConfigResponse> result = vipBoxFacade.getConfig();
		sessionWrite(session, response, result.item);
	}

}
