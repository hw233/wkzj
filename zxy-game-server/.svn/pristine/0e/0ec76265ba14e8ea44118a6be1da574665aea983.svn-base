package com.jtang.gameserver.module.demon.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.service.DemonGlobalService;
import com.jtang.gameserver.module.demon.facade.DemonFacade;
import com.jtang.gameserver.module.demon.handler.request.AttackBossRequest;
import com.jtang.gameserver.module.demon.handler.request.AttackPlayerRequest;
import com.jtang.gameserver.module.demon.handler.request.DemonExchangeRequest;
import com.jtang.gameserver.module.demon.handler.response.DemonCampDataResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonExchangeResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonLastRankResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonLastRewardResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonScoreResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonTimeResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class DemonHandler extends GatewayRouterHandlerImpl {

	@Autowired
	private DemonFacade demonFacade;

	@Override
	public byte getModule() {
		return ModuleName.DEMON;
	}

	@Cmd(Id = DemonCmd.DEMON_CAMP_DATA)
	public void getDemonData(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<DemonCampDataResponse> result = demonFacade.getDemonData(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}

		sessionWrite(session, response, result.item);
	}

	@Cmd(Id = DemonCmd.JOIN_CAMP)
	public void getJoin(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<DemonCampDataResponse> result = demonFacade.joinCamp(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}

		sessionWrite(session, response, result.item);

	}

	@Cmd(Id = DemonCmd.ATTACK_BOSS)
	public void attackBoss(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		AttackBossRequest attackBossRequest = new AttackBossRequest(bytes);
		boolean useTicket = attackBossRequest.useTicket == 1 ? true : false;
		;
		Result result = demonFacade.attackBoss(actorId, useTicket);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}

	}

	@Cmd(Id = DemonCmd.GET_DEMON_SCORE)
	public void getDemonScore(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		long score = demonFacade.getDemonScore(actorId);
		DemonScoreResponse demonScoreResponse = new DemonScoreResponse(score);
		sessionWrite(session, response, demonScoreResponse);

	}

	@Cmd(Id = DemonCmd.ATTACK_PLAYER)
	public void attackPlayer(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		AttackPlayerRequest attackPlayerRequest = new AttackPlayerRequest(bytes);
		boolean useTicket = attackPlayerRequest.useTicket == 1 ? true : false;
		Result result = demonFacade.attackPlayer(actorId, attackPlayerRequest.actorId, useTicket);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}

	}

	@Cmd(Id = DemonCmd.EXCHANGE_SCORE)
	public void exchangeScore(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		DemonExchangeRequest demonExchangeRequest = new DemonExchangeRequest(bytes);
		TResult<DemonExchangeResponse> result = demonFacade.exchangeReward(actorId, demonExchangeRequest.id, demonExchangeRequest.num);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			sessionWrite(session, response, result.item);
		}

	}

	@Cmd(Id = DemonCmd.VIEW_LAST_RANK)
	public void getLastRank(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<DemonLastRankResponse> result = demonFacade.getLastRankData(actorId);
		sessionWrite(session, response, result.item);
	}

	@Cmd(Id = DemonCmd.VIEW_LAST_REWARD)
	public void getLastReward(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<DemonLastRewardResponse> result = demonFacade.getLastRewardData(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			sessionWrite(session, response, result.item);
		}
	}

	@Cmd(Id = DemonCmd.SET_REWARD_READ)
	public void getSetReadReward(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Result result = demonFacade.setRewardRead(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

	@Cmd(Id = DemonCmd.GET_DEMON_TIME)
	public void getDemonTime(IoSession session, byte[] bytes, Response response) {
		Long time = DemonGlobalService.getOpenDate().getTime() / 1000;
		DemonTimeResponse demonTimeResponse = new DemonTimeResponse(time.intValue(), DemonGlobalService.getOpenTimes(),
				DemonGlobalService.getExchangeWeek(), DemonGlobalService.getExchangeTime());
		sessionWrite(session, response, demonTimeResponse);
	}

}
