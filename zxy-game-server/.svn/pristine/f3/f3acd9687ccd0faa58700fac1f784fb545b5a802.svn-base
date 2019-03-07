package com.jtang.gameserver.module.praise.handler;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.praise.facade.PraiseFacade;
import com.jtang.gameserver.module.praise.handler.request.PraiseRewardRequest;
import com.jtang.gameserver.module.praise.handler.response.PraiseDataResponse;
import com.jtang.gameserver.module.praise.handler.response.PraiseRewardResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class PraiseHandler extends GatewayRouterHandlerImpl {

	@Autowired
	private PraiseFacade praiseFacade;
	@Override
	public byte getModule() {
		return ModuleName.PRAISE;
	}
	
	@Cmd(Id = PraiseCmd.GET_PRAISE_DATA)
	public void getPraise(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<PraiseDataResponse> result = praiseFacade.getPraiseData(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {

			sessionWrite(session, response, result.item);
		}
	}
	@Cmd(Id = PraiseCmd.GET_REWARD)
	public void getReward(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		PraiseRewardRequest praiseRewardRequest = new PraiseRewardRequest(bytes);
		 TResult<List<RewardObject>> result = praiseFacade.getReward(actorId, praiseRewardRequest.praiseRewardType);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			TResult<PraiseDataResponse> r = praiseFacade.getPraiseData(actorId);
			if (r.isFail()) {
				response.setStatusCode(r.statusCode);
				sessionWrite(session, response);
			} else {
				PraiseRewardResponse praiseRewardResponse = new PraiseRewardResponse(r.item, result.item);
				sessionWrite(session, response, praiseRewardResponse);
			}
		}
	}

}
