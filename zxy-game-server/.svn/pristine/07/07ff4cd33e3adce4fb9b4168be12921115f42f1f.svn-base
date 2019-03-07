package com.jtang.gameserver.module.luckstar.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.luckstar.facade.LuckStarFacade;
import com.jtang.gameserver.module.luckstar.handler.response.LuckStarResponse;
import com.jtang.gameserver.module.luckstar.handler.response.LuckStarRewardResponse;
import com.jtang.gameserver.module.luckstar.module.LuckStarVO;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class LuckStarHandler extends GatewayRouterHandlerImpl {

	@Autowired
	LuckStarFacade luckStarFacade;

	@Override
	public byte getModule() {
		return ModuleName.LUCKSTAR;
	}

	@Cmd(Id = LuckStarCmd.LUCK_STAR_INFO)
	public void getLuckStar(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<LuckStarVO> result = luckStarFacade.getLuckStar(actorId);
		LuckStarResponse luckstar = new LuckStarResponse(result.item);
		response.setValue(luckstar.getBytes());
		sessionWrite(session, response);
	}

	@Cmd(Id = LuckStarCmd.GET_REWARD)
	public void getReward(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<LuckStarRewardResponse> result = luckStarFacade.getReward(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		} else {
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}

}
