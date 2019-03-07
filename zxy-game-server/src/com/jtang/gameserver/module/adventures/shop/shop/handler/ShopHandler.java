package com.jtang.gameserver.module.adventures.shop.shop.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Shop;
import com.jtang.gameserver.module.adventures.shop.shop.facade.BlackShopFacade;
import com.jtang.gameserver.module.adventures.shop.shop.facade.ShopFacade;
import com.jtang.gameserver.module.adventures.shop.shop.handler.request.BlackShopRequest;
import com.jtang.gameserver.module.adventures.shop.shop.handler.request.ShopRequest;
import com.jtang.gameserver.module.adventures.shop.shop.handler.response.ExchangeListResponse;
import com.jtang.gameserver.module.adventures.shop.shop.handler.response.ShopInfoResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class ShopHandler extends GatewayRouterHandlerImpl {

	
	@Autowired
	public ShopFacade shopFacade;
	@Autowired
	private BlackShopFacade blackShopFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.SHOP;
	}
	
	@Cmd(Id=ShopCmd.SHOP_INFO)
	public void shopInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Shop shop=shopFacade.getShops(actorId);
		ShopInfoResponse shopInfo=new ShopInfoResponse(shop.getShopVOs());
		sessionWrite(session, response, shopInfo);
	}
	
	@Cmd(Id=ShopCmd.BUY)
	public void buy(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		ShopRequest shopRequest=new ShopRequest(bytes);
		TResult<Integer> result=shopFacade.buy(actorId,shopRequest.shopId,shopRequest.num);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = ShopCmd.BLACK_SHOP_INFO)
	public void getBlackShop(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<ExchangeListResponse> result = blackShopFacade.getExchangeList(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = ShopCmd.BLACK_SHOP_BUY)
	public void buyBlackShop(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		BlackShopRequest request = new BlackShopRequest(bytes);
		Result result = blackShopFacade.exchange(actorId, request.exchangeId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = ShopCmd.BLACK_SHOP_FLUSH)
	public void flushBlackShop(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<ExchangeListResponse> result = blackShopFacade.flushExchange(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
}
