package com.jtang.gameserver.module.adventures.shop.trader.handler;

import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.shop.trader.facade.TraderFacade;
import com.jtang.gameserver.module.adventures.shop.trader.handler.request.ShopFlushRequest;
import com.jtang.gameserver.module.adventures.shop.trader.handler.request.ShopInfoRequest;
import com.jtang.gameserver.module.adventures.shop.trader.handler.request.ShopPermanentRequest;
import com.jtang.gameserver.module.adventures.shop.trader.handler.request.ShopRequest;
import com.jtang.gameserver.module.adventures.shop.trader.handler.response.ShopInfoResponse;
import com.jtang.gameserver.module.adventures.shop.trader.handler.response.ShopOpenResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class TraderHandler extends GatewayRouterHandlerImpl {
	
	@Autowired
	public TraderFacade traderFacade;

	@Override
	public byte getModule() {
		return ModuleName.TRADER;
	}
	
	@Cmd(Id=TraderCmd.TRADER_INFO)
	public void shopInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		ShopInfoRequest request = new ShopInfoRequest(bytes);
		TResult<ShopInfoResponse> result = traderFacade.getInfo(request.shopType,actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			sessionWrite(session, response, result.item);
		}
	}
	
	@Cmd(Id=TraderCmd.TRADER_BUY)
	public void shopBuy(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		ShopRequest request = new ShopRequest(bytes);
		Result result = traderFacade.shopBuy(request.shopType,actorId,request.shopId,request.num);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id=TraderCmd.TRADER_FLUSH)
	public void shopFlush(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		ShopFlushRequest request = new ShopFlushRequest(bytes);
		TResult<ShopInfoResponse> result = traderFacade.shopFlush(request.shopType,actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id=TraderCmd.TRADER_PERMANENT)
	public void buyPermanent(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		ShopPermanentRequest request = new ShopPermanentRequest(bytes);
		TResult<ShopInfoResponse> result = traderFacade.buyPermanent(request.shopType,actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id=TraderCmd.GET_SHOP_OPEN_INFO)
	public void getOpenInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<Map<Integer,Integer>> result = traderFacade.getOpenInfo(actorId);
		ShopOpenResponse rsp = new ShopOpenResponse(result.item);
		sessionWrite(session, response, rsp);
	}
}
