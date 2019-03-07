package com.jtang.gameserver.module.goods.handler;

import java.util.Collection;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.handler.request.SellGoodsRequest;
import com.jtang.gameserver.module.goods.handler.request.StartComposeRequest;
import com.jtang.gameserver.module.goods.handler.request.UseGoodsRequest;
import com.jtang.gameserver.module.goods.handler.response.GoodsListResponse;
import com.jtang.gameserver.module.goods.handler.response.SellGoodsResponse;
import com.jtang.gameserver.module.goods.handler.response.StartComposeResponse;
import com.jtang.gameserver.module.goods.handler.response.UseGoodsResponse;
import com.jtang.gameserver.module.goods.model.GoodsVO;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

/**
 * 物品业务逻辑接收接口
 * @author 0x737263
 *
 */
@Component
public class GoodsHandler extends GatewayRouterHandlerImpl {

	@Autowired
	GoodsFacade goodsFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.GOODS;
	}
	
	@Cmd(Id = GoodsCmd.GET_LIST)
	public void getInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Collection<GoodsVO> list = goodsFacade.getList(actorId);
		GoodsListResponse packet = GoodsListResponse.valueOf(list);
		sessionWrite(session, response, packet);
	}
	
	@Cmd(Id = GoodsCmd.USE_GOODS)
	public void useGoods(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		UseGoodsRequest request = new UseGoodsRequest(bytes);
        TResult<List<UseGoodsResult>> result = goodsFacade.useGoods(actorId, request.goodsId, request.useNum, request.useFlag, request.phoneNum);
        if (result.isOk()){
        	UseGoodsResponse useGoodsResponse = new UseGoodsResponse(result.item, request.goodsId);
        	sessionWrite(session, response,useGoodsResponse);
        } else {
        	response.setStatusCode(result.statusCode);
        	sessionWrite(session, response);
        }

	}
	
	@Cmd(Id = GoodsCmd.SELL_GOODS)
	public void sellGoods(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		SellGoodsRequest request = new SellGoodsRequest(bytes);
		TResult<Integer> result = goodsFacade.sellGoods(actorId,request.goodsId,request.goodsNum);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			SellGoodsResponse rsp = new SellGoodsResponse(result.item);
			response.setValue(rsp.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = GoodsCmd.COMPOSE_GOODS)
	public void composeGoods(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		StartComposeRequest request = new StartComposeRequest(bytes);
		TResult<StartComposeResponse> result = goodsFacade.composeGoods(actorId,request.goodsId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}

}
