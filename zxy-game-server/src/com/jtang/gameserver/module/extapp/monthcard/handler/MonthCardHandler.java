package com.jtang.gameserver.module.extapp.monthcard.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.monthcard.facade.MonthCardFacade;
import com.jtang.gameserver.module.extapp.monthcard.handler.response.MonthCardResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class MonthCardHandler extends GatewayRouterHandlerImpl {

	@Autowired
	MonthCardFacade monthCardFacade;
	
	@Autowired
	PlayerSession playerSession;
	
	@Override
	public byte getModule() {
		return ModuleName.MONTH_CARD;
	}
	
	@Cmd(Id = MonthCardCmd.GET_MONTH_CARD_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<MonthCardResponse> result = monthCardFacade.getInfo(actorId);
		sessionWrite(session, response,result.item);
	}
	

	@Cmd(Id = MonthCardCmd.GET_MONTH_CARD_REWARD)
	public void getReward(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = monthCardFacade.getReward(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session,response);
	}
	
	@Cmd(Id = MonthCardCmd.GET_LIFELONG_REWARD)
	public void getlifelongReward(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = monthCardFacade.getlifelongReward(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = MonthCardCmd.GET_YEAR_REWARD)
	public void getYearReward(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = monthCardFacade.getYearReward(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
}
