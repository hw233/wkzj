package com.jtang.gameserver.module.treasure.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.service.TreasureService;
import com.jtang.gameserver.module.treasure.facade.TreasureFacade;
import com.jtang.gameserver.module.treasure.handler.request.TreasureExchangeRequest;
import com.jtang.gameserver.module.treasure.handler.response.TreasureExchangeResponse;
import com.jtang.gameserver.module.treasure.handler.response.TreasureGoodsResponse;
import com.jtang.gameserver.module.treasure.handler.response.TreasureResponse;
import com.jtang.gameserver.module.treasure.model.TreasureVO;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class TreasureHandler extends GatewayRouterHandlerImpl {

	@Autowired
	TreasureFacade treasureFacade;

	@Override
	public byte getModule() {
		return ModuleName.MAZE_TREASURE;
	}

	@Cmd(Id = TreasureCmd.GET_TREASURE)
	public void getTreasure(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<TreasureVO> result = treasureFacade.getTreasure(actorId);
		if(result.isFail()){
			TreasureResponse treasureResponse = new TreasureResponse(false);
			sessionWrite(session, response,treasureResponse);
		}else{
			TreasureResponse treasureResponse = new TreasureResponse(result.item, TreasureService.getOpenTimes());
			sessionWrite(session, response, treasureResponse);
		}
	}

	@Cmd(Id = TreasureCmd.MOVE)
	public void move(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Result result = treasureFacade.move(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = TreasureCmd.EXCHANGE_REWARD)
	public void exchangeReward(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TreasureExchangeRequest request = new TreasureExchangeRequest(bytes);
		TResult<Integer> result = treasureFacade.exchangeReward(actorId,request.exchangeId,request.num);
		if(result.isOk()){
			TreasureExchangeResponse exchangeResponse = new TreasureExchangeResponse(result.item);
			response.setValue(exchangeResponse.getBytes());
		}
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = TreasureCmd.EXCHANGE_GOODS)
	public void exchangeGoods(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<Integer> result = treasureFacade.exchangeGoods(actorId);
		TreasureGoodsResponse exchangeResponse = new TreasureGoodsResponse(result.item);
		response.setValue(exchangeResponse.getBytes());
		sessionWrite(session, response);
	}
}
