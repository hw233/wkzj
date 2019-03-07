package com.jtang.gameserver.module.smelt.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.smelt.facade.SmeltFacade;
import com.jtang.gameserver.module.smelt.handler.request.SmeltExchangeRequest;
import com.jtang.gameserver.module.smelt.handler.request.SmeltRequest;
import com.jtang.gameserver.module.smelt.handler.response.SmeltInfoResponse;
import com.jtang.gameserver.module.smelt.handler.response.SmeltResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class SmeltHandler extends GatewayRouterHandlerImpl {

	@Autowired
	SmeltFacade smeltFacade;
	
	@Autowired
	GoodsFacade goodsFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.SMELT;
	}
	
	@Cmd(Id=SmeltCmd.SMELT_CONVERT)
	public void smeltConvert(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		SmeltRequest smeltRequest = new SmeltRequest(bytes);
		TResult<SmeltResponse> result = smeltFacade.convert(actorId,smeltRequest.heroId,smeltRequest.num);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id=SmeltCmd.SMELT_EXCHANGE_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<SmeltInfoResponse> result = smeltFacade.getExchangeInfo(actorId);
		response.setValue(result.item.getBytes());
		sessionWrite(session, response);
	}
	
	@Cmd(Id=SmeltCmd.SMELT_EXCHANGE)
	public void exchange(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		SmeltExchangeRequest request = new SmeltExchangeRequest(bytes);
		Result result = smeltFacade.exchange(actorId,request.heroId,request.num);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
}
