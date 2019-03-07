package com.jtang.gameserver.admin.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.GoodsMaintianFacade;
import com.jtang.gameserver.admin.handler.request.AddAllGoodsRequest;
import com.jtang.gameserver.admin.handler.request.DeleteGoodsRequest;
import com.jtang.gameserver.admin.handler.request.GiveGoodsRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;
@Component
public class GoodsMaintianHandler extends AdminRouterHandlerImpl {
	
	protected final Log LOGGER = LogFactory.getLog(GoodsMaintianHandler.class);
	
	@Autowired
	GoodsMaintianFacade goodsMaintianFacade;
	
	@Cmd(Id = GoodsMaintianCmd.ADD_GOODS)
	public void giveGoods(IoSession session, byte[] bytes, Response response) {
		GiveGoodsRequest giveGoodsRequest = new GiveGoodsRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+giveGoodsRequest.actorId+"----- goodsId = "+ giveGoodsRequest.goodsId+" ----- num = "+giveGoodsRequest.num);
		}
		Result result = goodsMaintianFacade.addGoods(giveGoodsRequest.actorId, giveGoodsRequest.goodsId, giveGoodsRequest.num);
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = GoodsMaintianCmd.DEL_GOODS)
	public void delGoods(IoSession session, byte[] bytes, Response response){
		DeleteGoodsRequest deleteGoodsRequest = new DeleteGoodsRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+deleteGoodsRequest.actorId+"----- goodsId = "+ deleteGoodsRequest.goodsId+" ----- num = "+deleteGoodsRequest.num);
		}
		Result result = goodsMaintianFacade.deleteGoods(deleteGoodsRequest.actorId, deleteGoodsRequest.goodsId, deleteGoodsRequest.num);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

	
	@Cmd(Id = GoodsMaintianCmd.ADD_ALL_GOODS)
	public void addAllGoods(IoSession session, byte[] bytes, Response response){
		AddAllGoodsRequest addAllGoodsRequest = new AddAllGoodsRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+addAllGoodsRequest.actorId+"----- add AllGoods");
		}
		
		Result result = goodsMaintianFacade.addAllGoods(addAllGoodsRequest.actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

	@Override
	public byte getModule() {
		return GameAdminModule.GOODS;
	}
}
