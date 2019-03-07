package com.jtang.gameserver.module.sign.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.sign.facade.SignFacade;
import com.jtang.gameserver.module.sign.handler.response.SignInfoResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
@Component
public class SignHandler extends GatewayRouterHandlerImpl{

	@Autowired
	private SignFacade signFacade;
	@Override
	public byte getModule() {
		return ModuleName.SIGN;
	}
	
	
	@Cmd(Id = SignCmd.SIGN_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<SignInfoResponse> result = signFacade.getInfo(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = SignCmd.SIGN)
	public void sign(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<SignInfoResponse> result = signFacade.sign(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session,response);
	}
	
	@Cmd(Id = SignCmd.VIP_SIGN)
	public void vipSign(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<SignInfoResponse> result = signFacade.vipSign(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
}
