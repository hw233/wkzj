package com.jtang.gameserver.module.recruit.handler;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.recruit.facade.RecruitFacade;
import com.jtang.gameserver.module.recruit.handler.request.RandRecruitRequest;
import com.jtang.gameserver.module.recruit.handler.response.GetInfoResponse;
import com.jtang.gameserver.module.recruit.handler.response.RandRecruitResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

/**
 * 聚仙阵handler
 * @author 0x737263
 *
 */
@Component
public class RecruitHandler extends GatewayRouterHandlerImpl {
	
	@Autowired
	private RecruitFacade recruitFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.RECRUIT;
	}
	
	/**
	 * 获取聚仙阵信息
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = RecruitCmd.GET_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		GetInfoResponse packet = recruitFacade.getInfo(actorId);
		sessionWrite(session, response, packet);
	}
	@Cmd(Id = RecruitCmd.RAND)
	public void rand(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		RandRecruitRequest randRecruitRequest = new RandRecruitRequest(bytes);
		TResult<List<RewardObject>> result = recruitFacade.randRecruit(actorId, randRecruitRequest.recruitType, randRecruitRequest.single);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			GetInfoResponse info = recruitFacade.getInfo(actorId);
			RandRecruitResponse randRecruitResponse = new RandRecruitResponse(info, result.item);
			sessionWrite(session, response, randRecruitResponse);
		}
	}
	

	

}
