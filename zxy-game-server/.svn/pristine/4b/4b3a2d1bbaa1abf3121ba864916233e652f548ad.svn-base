package com.jtang.gameserver.module.adventures.achievement.handler;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.module.adventures.achievement.facade.AchieveFacade;
import com.jtang.gameserver.module.adventures.achievement.handler.request.GetRewardRequest;
import com.jtang.gameserver.module.adventures.achievement.handler.response.AchieveListResponse;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
@Component
public class AchieveHandler extends GatewayRouterHandlerImpl{
	
	@Autowired
	private AchieveFacade achievementFacade;
	@Override
	public byte getModule() {
		return ModuleName.ACHIEVEMENT;
	}
	@Cmd(Id = AchieveCmd.GET_ACHIVEMENT)
	public void getAchievement(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		List<AchieveVO> achievementList = achievementFacade.getAchieve(actorId);
		AchieveListResponse packet = new AchieveListResponse(achievementList);
		sessionWrite(session, response, packet);
	}
	
	@Cmd(Id = AchieveCmd.GET_REWARD)
	public void getReward(IoSession session, byte[] bytes, Response response) {
		GetRewardRequest request = new GetRewardRequest(bytes);
		Result result = achievementFacade.getReward(playerSession.getActorId(session), request.achieveId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
}
