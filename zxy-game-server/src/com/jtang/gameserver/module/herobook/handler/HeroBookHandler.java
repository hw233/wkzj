//package com.jtang.gameserver.module.herobook.handler;
//
//import org.apache.mina.core.session.IoSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.jiatang.common.ModuleName;
//import com.jtang.core.mina.router.annotation.Cmd;
//import com.jtang.core.protocol.Response;
//import com.jtang.core.result.TResult;
//import com.jtang.gameserver.module.herobook.facade.HeroBookFacade;
//import com.jtang.gameserver.module.herobook.handler.request.HeroBookRewardRequest;
//import com.jtang.gameserver.module.herobook.handler.response.HeroBookResponse;
//import com.jtang.gameserver.module.herobook.handler.response.HeroBookRewardResponse;
//import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
//@Component
//public class HeroBookHandler extends GatewayRouterHandlerImpl {
//
//	@Autowired
//	private HeroBookFacade heroBookFacade;
//	@Override
//	public byte getModule() {
//		return ModuleName.HERO_BOOK;
//	}
//	
//	@Cmd (Id = HeroBookCmd.GET_HERO_BOOK)
//	public void getHeroBook(IoSession session, byte[] bytes, Response response) {
//		long actorId = playerSession.getActorId(session);
//		HeroBookResponse heroBookResponse = heroBookFacade.getHeroBookData(actorId);
//		sessionWrite(session, response, heroBookResponse);
//	}
//	@Cmd (Id = HeroBookCmd.GET_REWARD)
//	public void getReward(IoSession session, byte[] bytes, Response response) {
//		long actorId = playerSession.getActorId(session);
//		HeroBookRewardRequest heroBookRewardRequest = new HeroBookRewardRequest(bytes);
//		TResult<HeroBookRewardResponse> result = heroBookFacade.getReward(actorId, heroBookRewardRequest.orderId);
//		if (result.isFail()) {
//			response.setStatusCode(result.statusCode);
//			sessionWrite(session, response);
//		} else {
//			sessionWrite(session, response, result.item);
//		}
//	}
//
//}
