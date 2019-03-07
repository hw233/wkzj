package com.jtang.gameserver.module.extapp.onlinegifts.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.onlinegifts.facade.OnlineGiftsFacade;
import com.jtang.gameserver.module.extapp.onlinegifts.handler.response.OnlineGiftsInfoResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class OnlineGiftsHandler extends GatewayRouterHandlerImpl {

	@Autowired
	OnlineGiftsFacade onlineGiftsFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.ONLINEGIFTS;
	}
	
	@Cmd(Id = OnlineGiftsCmd.ONLINE_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<OnlineGiftsInfoResponse> result = onlineGiftsFacade.getOnlineInfo(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			sessionWrite(session, response, result.item);
		}
	}
	
	@Cmd(Id = OnlineGiftsCmd.RECEIVE)
	public void receive(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = onlineGiftsFacade.receiveGift(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
}
