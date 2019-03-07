package com.jtang.worldserver.module.crossbattle.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jiatang.common.crossbattle.CrossBattleCmd;
import com.jiatang.common.crossbattle.request.AttackActorG2W;
import com.jiatang.common.crossbattle.request.AttackActorResultG2W;
import com.jiatang.common.crossbattle.request.ExchangePointG2W;
import com.jiatang.common.crossbattle.request.GetLineupG2W;
import com.jiatang.common.crossbattle.request.SignupG2W;
import com.jiatang.common.crossbattle.response.ActorCrossDataW2G;
import com.jiatang.common.crossbattle.response.ActorPointW2G;
import com.jiatang.common.crossbattle.response.AttackPlayerW2G;
import com.jiatang.common.crossbattle.response.CrossBattleConfigW2G;
import com.jiatang.common.crossbattle.response.EndRewardW2G;
import com.jiatang.common.crossbattle.response.ExchangePointW2G;
import com.jiatang.common.crossbattle.response.HomeServerRankW2G;
import com.jiatang.common.crossbattle.response.LastBattleResultW2G;
import com.jiatang.common.crossbattle.response.LineupW2G;
import com.jiatang.common.crossbattle.response.ServerRankW2G;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.ActorRequest;
import com.jtang.core.protocol.ActorResponse;
import com.jtang.core.result.TResult;
import com.jtang.worldserver.module.crossbattle.facade.CrossBattleFacade;
import com.jtang.worldserver.server.router.WorldRouterHandlerImpl;

@Component
public class CrossBattleG2WHandler extends WorldRouterHandlerImpl {
	@Autowired
	private CrossBattleFacade crossBattleFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.CROSS_BATTLE;
	}
		
	@Cmd(Id = CrossBattleCmd.ATTACK_ACTOR)
	public void attackActor(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
		AttackActorG2W attackPlayerRequest = new AttackActorG2W(request.getValue());
		TResult<AttackPlayerW2G> result = crossBattleFacade.attackPlayer(serverId, request.getActorId(), attackPlayerRequest.targetActorId);
		if (result.isOk()) {
			response.setValue(result.item.getBytes());
		} else {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	@Cmd(Id = CrossBattleCmd.G2W_POST_BATTLE_RESULT)
	public void attackActorResult(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
		AttackActorResultG2W attackPlayerResultG2W = new AttackActorResultG2W(request.getValue());
		crossBattleFacade.attackPlayerResult(serverId,request.getActorId(), attackPlayerResultG2W);
	}
		
	@Cmd(Id = CrossBattleCmd.GET_CROSS_DATA)
	public void getCrossData(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
//		ActorCrossG2W actorCrossG2W = new ActorCrossG2W(request.getValue());
		TResult<ActorCrossDataW2G> result = crossBattleFacade.getCrossData(serverId, request.getActorId());
		if (result.isOk()) {
			response.setValue(result.item.getBytes());
		} else {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = CrossBattleCmd.GET_LINEUP)
	public void getLineup(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
		GetLineupG2W getLineupG2W = new GetLineupG2W(request.getValue());
		TResult<LineupW2G> result = crossBattleFacade.getLineup(serverId, request.getActorId(), getLineupG2W.targetActorId);
		if (result.isOk()) {
			response.setValue(result.item.getBytes());
		} else {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = CrossBattleCmd.GET_SERVER_SCORE_LIST)
	public void getServerRank(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
//		GetServerRankG2W getServerRankG2W = new GetServerRankG2W(request.getValue());
		TResult<ServerRankW2G> result = crossBattleFacade.getServerRank(serverId, request.getActorId());
		if (result.isOk()) {
			response.setValue(result.item.getBytes());
		} else {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = CrossBattleCmd.GET_HOME_SERVER_RANK)
	public void getHomeServerRank(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
//		GetHomeServerRankG2W getHomeServerRankG2W = new GetHomeServerRankG2W(request.getValue());
		TResult<HomeServerRankW2G> result = crossBattleFacade.getHomeServerRank(serverId, request.getActorId()) ;
		if (result.isOk()) {
			response.setValue(result.item.getBytes());
		} else {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = CrossBattleCmd.DAY_BATTLE_END_REWARD_RESULT)
	public void dayBattleEndRewardResult(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
//		GetEndRewardG2W getEndRewardG2W = new GetEndRewardG2W(request.getValue());
		TResult<EndRewardW2G> result = crossBattleFacade.dayBattleEndRewardResult(serverId, request.getActorId());
		if (result.isOk()) {
			response.setValue(result.item.getBytes());
		} else {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = CrossBattleCmd.EXCHANGE_POINT)
	public void exchangePoint(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
		ExchangePointG2W exchangePointG2W = new ExchangePointG2W(request.getValue());
		TResult<ExchangePointW2G> result = crossBattleFacade.exchangePoint(serverId, request.getActorId(), exchangePointG2W.configId) ;
		if (result.isOk()) {
			response.setValue(result.item.getBytes());
		} else {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	@Cmd(Id = CrossBattleCmd.GET_CROSS_BATTLE_CONFIG)
	public void getConfig(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
//		CrossBattleConfigG2W battleConfigG2W = new CrossBattleConfigG2W(request.getValue());
		TResult<CrossBattleConfigW2G> result = crossBattleFacade.getConfig(request.getActorId());
		if (result.isOk()) {
			response.setValue(result.item.getBytes());
		} else {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = CrossBattleCmd.GET_LAST_BATTLE_RESULT)
	public void getLastBattleResult(IoSession session, int serverId, ActorRequest request, ActorResponse response){
//		LastBattleResultG2W lastbattleResultG2W = new LastBattleResultG2W(request.getValue());
		TResult<LastBattleResultW2G> result = crossBattleFacade.getLastBattleResult(request.getActorId());
		if (result.isOk()) {
			response.setValue(result.item.getBytes());
		} else {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = CrossBattleCmd.GET_ACTOR_POINT)
	public void getActorPoint(IoSession session, int serverId, ActorRequest request, ActorResponse response){
//		ActorPointG2W actorPointG2W = new ActorPointG2W(request.getValue());
		TResult<ActorPointW2G> result = crossBattleFacade.getActorPoint(request.getActorId(),serverId);
		if (result.isOk()) {
			response.setValue(result.item.getBytes());
		} else {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	@Cmd(Id = CrossBattleCmd.SET_READ_FLAG)
	public void setReadFlag(IoSession session, int serverId, ActorRequest request, ActorResponse response){
		crossBattleFacade.setReadFlag(request.getActorId());
		sessionWrite(session, response);
	}
	
	
	
	//==========================================================
	//以下为world-server 与game-server 之间的内部协议
	//==========================================================
	
	@Cmd(Id = CrossBattleCmd.W2G_SIGN_UP)
	public void signup(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
		SignupG2W signupRequest = new SignupG2W(request.getValue());
		crossBattleFacade.signup(serverId, signupRequest);
	}
	
//	@Cmd(Id = CrossBattleCmd.G2W_POST_BATTLE_RESULT)
//	public void attackPlayerResult(IoSession session, int serverId, ActorRequest request, ActorResponse response) {
//		AttackPlayerResultG2W attackPlayerResultRequest = new AttackPlayerResultG2W(bytes);
//		boolean result = crossBattleFacade.attackPlayerResult(serverId, attackPlayerResultRequest);
//		AttackPlayerResultW2G attackPlayerResultResponse = new AttackPlayerResultW2G(attackPlayerResultRequest.actorId, result);
//		sessionWrite(session, response, attackPlayerResultResponse);
//	}

}
