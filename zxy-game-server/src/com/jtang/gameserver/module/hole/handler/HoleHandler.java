package com.jtang.gameserver.module.hole.handler;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.hole.facade.HoleFacade;
import com.jtang.gameserver.module.hole.handler.request.HoleAllyOpenRequest;
import com.jtang.gameserver.module.hole.handler.request.HoleFightRequest;
import com.jtang.gameserver.module.hole.handler.request.HoleRequest;
import com.jtang.gameserver.module.hole.handler.request.HoleRewardRequest;
import com.jtang.gameserver.module.hole.handler.response.HoleResponse;
import com.jtang.gameserver.module.hole.handler.response.HoleRewardResponse;
import com.jtang.gameserver.module.hole.model.HoleVO;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class HoleHandler extends GatewayRouterHandlerImpl {

	@Autowired
	HoleFacade holeFacade;

	@Override
	public byte getModule() {
		return ModuleName.HOLE;
	}

	@Cmd(Id = HoleCmd.GET_HOLE_INFO)
	public void getHoleInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		HoleResponse holeResponse = holeFacade.getHoleResponse(actorId);
		sessionWrite(session, response, holeResponse);
	}

	@Cmd(Id = HoleCmd.GET_HOLE_REWARD)
	public void getReward(IoSession session, byte[] bytes, Response response) {
		HoleRewardRequest rewardRequest = new HoleRewardRequest(bytes);
		long actorId = playerSession.getActorId(session);
		TResult<List<RewardObject>> result = holeFacade.getReward(rewardRequest.id, actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			HoleRewardResponse holeRewardResponse = new HoleRewardResponse(result.item);
			sessionWrite(session, response, holeRewardResponse);
		}
	}

	@Cmd(Id = HoleCmd.HOLE_FIGHT)
	public void holeFight(IoSession session, byte[] bytes, Response response) {
		HoleFightRequest fightRequest = new HoleFightRequest(bytes);
		long actorId = playerSession.getActorId(session);
		Result res = holeFacade.holeFight(fightRequest.id, actorId, fightRequest.battleId);
		if(res.isFail()){
			response.setStatusCode(res.statusCode);
			sessionWrite(session, response);
		}
	}

	@Cmd(Id = HoleCmd.SEND_ALLY)
	public void sendAlly(IoSession session, byte[] bytes, Response response) {
		HoleRequest holeRequest = new HoleRequest(bytes);
		long id = holeRequest.id;
		long actorId = playerSession.getActorId(session);
		Result result = holeFacade.sendAlly(id, actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}

	@Cmd(Id = HoleCmd.GET_HOLE_PACKAGE_GIFT)
	public void getPackageGift(IoSession session, byte[] bytes, Response response) {
		HoleRequest holeRequest = new HoleRequest(bytes);
		long id = holeRequest.id;
		long actorId = playerSession.getActorId(session);
		Result result = holeFacade.getPackageGift(id, actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = HoleCmd.ALLY_OPEN_HOLE)
	public void allyOpenHole(IoSession session, byte[] bytes, Response response) {
		HoleAllyOpenRequest request = new HoleAllyOpenRequest(bytes);
		TResult<HoleVO> result = holeFacade.allyOpenHole(request.actorId, request.targetId,request.id);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			sessionWrite(session, response, result.item);
		}
	}

}
