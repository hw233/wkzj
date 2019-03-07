package com.jtang.gameserver.module.extapp.welkin.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.welkin.facade.WelkinFacade;
import com.jtang.gameserver.module.extapp.welkin.handler.request.WelkinRequest;
import com.jtang.gameserver.module.extapp.welkin.handler.response.WelkinRankResponse;
import com.jtang.gameserver.module.extapp.welkin.handler.response.WelkinResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class WelkinHandler extends GatewayRouterHandlerImpl {

	@Autowired
	WelkinFacade welkinFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.WELKIN;
	}

	
	@Cmd(Id = WelkinCmd.WELKIN_INFO)
	public void getWelkinInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<WelkinResponse> result = welkinFacade.getWelkinInfo(actorId);
		response.setValue(result.item.getBytes());
		sessionWrite(session, response);
	}
	
	@Cmd(Id = WelkinCmd.WELKIN)
	public void welkin(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		WelkinRequest request = new WelkinRequest(bytes);
		TResult<WelkinResponse> result = welkinFacade.welkin(actorId,request.count);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = WelkinCmd.WELKIN_RANK)
	public void welkinRank(IoSession session, byte[] bytes, Response response){
		TResult<WelkinRankResponse> result = welkinFacade.getRank();
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
}
