package com.jtang.gameserver.module.power.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.power.facade.PowerFacade;
import com.jtang.gameserver.module.power.handler.request.PowerExchangeRequest;
import com.jtang.gameserver.module.power.handler.request.PowerFightRequest;
import com.jtang.gameserver.module.power.handler.response.PowerFlushResponse;
import com.jtang.gameserver.module.power.handler.response.PowerInfoResponse;
import com.jtang.gameserver.module.power.handler.response.PowerShopInfoResponse;
import com.jtang.gameserver.module.power.handler.response.RankListResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class PowerHandler extends GatewayRouterHandlerImpl {
	@Autowired
	private PowerFacade powerFacade;
	
	@Autowired
	private LineupFacade lineupFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.POWER;
	}
	
	@Cmd(Id = PowerCmd.GET_POWER_INFO)
	public void getPowerInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<PowerInfoResponse> result = powerFacade.getInfo(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			sessionWrite(session, response,result.item);
		}
	}
	
	@Cmd(Id = PowerCmd.POWER_FIGHT)
	public void powerFight(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		PowerFightRequest request = new PowerFightRequest(bytes);
		if(request.targetActorId == 0){
			response.setStatusCode(GameStatusCodeConstant.ACTOR_ID_ERROR);
			sessionWrite(session, response);
		}else{
			Result result = powerFacade.challenge(actorId, request.targetActorId);
			if(result.isFail()){
				response.setStatusCode(result.statusCode);
				sessionWrite(session, response);
			}
		}
	}
	
	@Cmd(Id = PowerCmd.BUY_FIGHT_NUM)
	public void buyFightNum(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<PowerFlushResponse> result = powerFacade.buyFightNum(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			sessionWrite(session, response, result.item);
		}
	}
	
	@Cmd(Id = PowerCmd.EXCHANGE)
	public void exchange(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		PowerExchangeRequest request = new PowerExchangeRequest(bytes);
		Result result  = powerFacade.exchange(actorId,request.exchangeId,request.exchangeNum);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = PowerCmd.GET_TOP_RANK)
	public void getTopRank(IoSession session,byte[] bytes,Response response){
		TResult<RankListResponse> result = powerFacade.getTopRank();
		sessionWrite(session, response,result.item);
	}
	
	@Cmd(Id = PowerCmd.GET_FIGHT_RANK)
	public void getFightRank(IoSession session,byte[] bytes,Response response){
		long actorId = playerSession.getActorId(session);
		TResult<RankListResponse> result = powerFacade.getFightRank(actorId);
		sessionWrite(session, response,result.item);
	}
	
	@Cmd(Id = PowerCmd.GET_POWER_SHOP)
	public void getShopInfo(IoSession session,byte[] bytes,Response response){
		long actorId = playerSession.getActorId(session);
		TResult<PowerShopInfoResponse> result = powerFacade.getShopInfo(actorId);
		sessionWrite(session, response,result.item);
	}
	
	@Cmd(Id = PowerCmd.SHOP_FLUSH)
	public void shopFlush(IoSession session,byte[] bytes,Response response){
		long actorId = playerSession.getActorId(session);
		TResult<PowerShopInfoResponse> result = powerFacade.shopFlush(actorId);
		sessionWrite(session, response,result.item);
	}
}
