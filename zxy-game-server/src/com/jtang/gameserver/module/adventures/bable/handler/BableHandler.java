package com.jtang.gameserver.module.adventures.bable.handler;

import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.bable.facade.BableFacade;
import com.jtang.gameserver.module.adventures.bable.facade.BableRankFacade;
import com.jtang.gameserver.module.adventures.bable.handler.request.BableAutoRequest;
import com.jtang.gameserver.module.adventures.bable.handler.request.BableDataRequest;
import com.jtang.gameserver.module.adventures.bable.handler.request.ExchangeGoodsRequst;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableAutoResponse;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableDataResponse;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableExcangeGoodsResponse;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableRankResponse;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableSkipResponse;
import com.jtang.gameserver.module.adventures.bable.model.BableRankVO;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class BableHandler extends GatewayRouterHandlerImpl{

	
	@Autowired
	BableFacade bableFacade;
	
	@Autowired
	private BableRankFacade bableRankFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.BABLE;
	}
	
	@Cmd(Id=BableCmd.GET_INFO)
	public void bableInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		BableDataRequest request = new BableDataRequest(bytes);
		TResult<BableDataResponse> result = bableFacade.getBableInfo(actorId,request.bableType);
		sessionWrite(session, response,result.item);
	}
	
	@Cmd(Id=BableCmd.CHOICE_BABLE)
	public void choiceBattle(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		BableDataRequest request = new BableDataRequest(bytes);
		TResult<BableDataResponse> result = bableFacade.choiceBable(actorId,request.bableType);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id=BableCmd.START_BATTLE)
	public void startBattle(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = bableFacade.startBattle(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id=BableCmd.EXCHANGE_GOODS)
	public void exchangeGoods(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		ExchangeGoodsRequst request = new ExchangeGoodsRequst(bytes);
		TResult<BableExcangeGoodsResponse> result = bableFacade.exchange(actorId,request.exchangeId,request.exchangeNum);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = BableCmd.GET_EXCHANGE_GOODS_DATA)
	public void getExchangeList(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<BableExcangeGoodsResponse> result = bableFacade.getExchangeList(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id=BableCmd.RESET_BABLE)
	public void resetBable(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<BableDataResponse> result = bableFacade.resetBable(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session,response);
	}
	
	@Cmd(Id=BableCmd.SKIP_FLOOR)
	public void skipFloor(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<BableSkipResponse> result = bableFacade.skipFloorReward(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session,response);
	}
	@Cmd(Id=BableCmd.AUTO_BABLE)
	public void autoFloor(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		BableAutoRequest bableAutoRequest = new BableAutoRequest(bytes);
		TResult<BableAutoResponse> result = bableFacade.skipFloor(actorId, bableAutoRequest.bableType, bableAutoRequest.useGoodsId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session,response);
	}
	
	@Cmd(Id = BableCmd.GET_RANK)
	public void getRunk(IoSession session, byte[] bytes, Response response){
		Map<Integer,List<BableRankVO>> ranks = bableRankFacade.getRank();
		BableRankResponse bableRankResponse = new BableRankResponse(ranks);
		sessionWrite(session, response, bableRankResponse);
	}
	
}
