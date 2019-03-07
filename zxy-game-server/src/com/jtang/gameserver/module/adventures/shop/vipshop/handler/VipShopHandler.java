package com.jtang.gameserver.module.adventures.shop.vipshop.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.shop.vipshop.facade.VipShopFacade;
import com.jtang.gameserver.module.adventures.shop.vipshop.handler.request.VipShopBuyRequest;
import com.jtang.gameserver.module.adventures.shop.vipshop.handler.response.VipShopResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class VipShopHandler extends GatewayRouterHandlerImpl {

	@Autowired
	VipShopFacade vipShopFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.VIP_SHOP;
	}

	@Cmd(Id = VipShopCmd.GET_SHOP_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<VipShopResponse> result = vipShopFacade.getInfo(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = VipShopCmd.BUY)
	public void buy(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		VipShopBuyRequest request = new VipShopBuyRequest(bytes);
		Result result = vipShopFacade.buy(actorId,request.id,request.num);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
}
