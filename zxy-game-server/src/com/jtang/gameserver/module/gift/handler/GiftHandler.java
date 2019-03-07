package com.jtang.gameserver.module.gift.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.MapResult;
import com.jtang.core.result.Result;
import com.jtang.gameserver.dbproxy.entity.Gift;
import com.jtang.gameserver.module.gift.facade.GiftFacade;
import com.jtang.gameserver.module.gift.handler.request.AcceptGiftRequest;
import com.jtang.gameserver.module.gift.handler.response.AcceptGiftResponse;
import com.jtang.gameserver.module.gift.handler.response.GiftInfoResponse;
import com.jtang.gameserver.module.gift.handler.response.OpenGiftPackageResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

/**
 * 礼物模块响应请求类
 * @author vinceruan
 *
 */
@Component
public class GiftHandler extends GatewayRouterHandlerImpl {

	@Autowired
	GiftFacade giftFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.GIFT;
	}
	
	@Cmd(Id=GiftCmd.ACCEPT_GIFT)
	public void acceptGift(IoSession session, byte[] bytes, Response response) {		
		AcceptGiftRequest request = new AcceptGiftRequest(bytes);
		long allyActorId = request.allyActorId;
		long actorId = playerSession.getActorId(session);
		MapResult<Long, Integer> res = this.giftFacade.acceptGift(actorId, allyActorId);
		response.setStatusCode(res.statusCode);
		if (res.isOk()) {
			AcceptGiftResponse rsp = new AcceptGiftResponse(res.item);
			sessionWrite(session, response, rsp);
		} else {
			sessionWrite(session, response);
		}
	}

	@Cmd(Id=GiftCmd.GET_GIFT_INFO)
	public void getGiftInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Gift gift = this.giftFacade.get(actorId);
		GiftInfoResponse rsp = new GiftInfoResponse(gift);
		sessionWrite(session, response, rsp);
	}

	@Cmd(Id=GiftCmd.GIVE_GIFT)
	public void giveGift(IoSession session, byte[] bytes, Response response) {
		AcceptGiftRequest request = new AcceptGiftRequest(bytes);
		long allyActorId = request.allyActorId;
		long actorId = playerSession.getActorId(session);
		Result res = this.giftFacade.giveGift(actorId, allyActorId);
		response.setStatusCode(res.statusCode);
		sessionWrite(session, response);
	}

	@Cmd(Id=GiftCmd.OPEN_GIFT_PACKAGE)
	public void openGiftPackage(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		MapResult<Long, Integer> res= giftFacade.openGiftPackage(actorId);
		response.setStatusCode(res.statusCode);
		if (res.isOk()) {
			OpenGiftPackageResponse rsp = new OpenGiftPackageResponse(res.item);
			sessionWrite(session, response, rsp);
		} else {
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = GiftCmd.ONE_KEY_GIVE_GIFT)
	public void oneKeyGiveGift(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = giftFacade.oneKeyGiveGift(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = GiftCmd.ONE_KEY_ACCEPT_GIFT)
	public void oneKeyAcceptGift(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		MapResult<Long, Integer> result = giftFacade.oneKeyAcceptGift(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
}
