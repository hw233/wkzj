package com.jtang.gameserver.module.extapp.basin.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.basin.facade.BasinFacade;
import com.jtang.gameserver.module.extapp.basin.handler.request.BasinRewardRequest;
import com.jtang.gameserver.module.extapp.basin.handler.response.BasinResponse;
import com.jtang.gameserver.module.extapp.basin.handler.response.BasinStateResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class BasinHandler extends GatewayRouterHandlerImpl {

	@Autowired
	public BasinFacade basinFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.BASIN;
	}
	
	@Cmd(Id = BasinCmd.GET_BASIN)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<BasinResponse> result = basinFacade.getInfo(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			sessionWrite(session, response, result.item);
		}
	}
	
	@Cmd(Id = BasinCmd.GET_REWARD)
	public void getReward(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		BasinRewardRequest request = new BasinRewardRequest(bytes);
		Result result = basinFacade.getReward(actorId,request.recharge);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = BasinCmd.IS_OPEN)
	public void getState(IoSession session, byte[] bytes, Response response){
		TResult<BasinStateResponse> result = basinFacade.getState();
		response.setValue(result.item.getBytes());
		sessionWrite(session, response);
	}
}
