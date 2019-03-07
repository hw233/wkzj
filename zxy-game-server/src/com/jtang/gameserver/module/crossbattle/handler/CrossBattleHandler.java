package com.jtang.gameserver.module.crossbattle.handler;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.ModuleName;
import com.jiatang.common.crossbattle.CrossBattleCmd;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.crossbattle.facade.CrossBattleCallbackFacade;
import com.jtang.gameserver.module.crossbattle.facade.CrossBattleRewardFacade;
import com.jtang.gameserver.module.crossbattle.handler.response.CrossBattleIsRewardResponse;
import com.jtang.gameserver.module.crossbattle.handler.response.CrossBattleRewardResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
import com.jtang.gameserver.server.session.PlayerSession;
import com.jtang.gameserver.worldclient.session.WorldClientSession;

@Component
public class CrossBattleHandler extends GatewayRouterHandlerImpl {

	@Autowired
	WorldClientSession worldClientSession;
	
	@Autowired
	PlayerSession playerSession;
	
	@Autowired
	CrossBattleCallbackFacade corssBattleCallbackFacade;
	
	@Autowired
	CrossBattleRewardFacade crossBattleRewardFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.CROSS_BATTLE;
	}
	
	
	@Cmd(Id = CrossBattleCmd.GET_CROSS_DATA)
	public void getCrossData(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		boolean flag = worldClientSession.game2WorldForward(response, actorId);
		if (flag == false) {
			response.setStatusCode(GameStatusCodeConstant.WORLD_SERVER_NOT_EXSIT);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = CrossBattleCmd.GET_LINEUP)
	public void getLineup(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
//		ViewLineupRequest viewLineupRequest = new ViewLineupRequest(bytes);
		boolean flag = worldClientSession.game2WorldForward(response, actorId, bytes);
		if (flag == false) {
			response.setStatusCode(GameStatusCodeConstant.WORLD_SERVER_NOT_EXSIT);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = CrossBattleCmd.ATTACK_ACTOR)
	public void attackActor(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Result result = corssBattleCallbackFacade.attackPlayer();
		if(result.isOk()){
			boolean flag = worldClientSession.game2WorldForward(response, actorId, bytes);
			if (flag == false) {
				response.setStatusCode(GameStatusCodeConstant.WORLD_SERVER_NOT_EXSIT);
				sessionWrite(session, response);
			}
		}else{
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = CrossBattleCmd.GET_SERVER_SCORE_LIST)
	public void getServerRank(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);		
		boolean flag = worldClientSession.game2WorldForward(response, actorId);
		if (flag == false) {
			response.setStatusCode(GameStatusCodeConstant.WORLD_SERVER_NOT_EXSIT);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = CrossBattleCmd.GET_HOME_SERVER_RANK)
	public void getHomeServerRank(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		boolean flag = worldClientSession.game2WorldForward(response, actorId);
		if (flag == false) {
			response.setStatusCode(GameStatusCodeConstant.WORLD_SERVER_NOT_EXSIT);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = CrossBattleCmd.DAY_BATTLE_END_REWARD_RESULT)
	public void dayBattleEndRewardResult(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		boolean flag = worldClientSession.game2WorldForward(response, actorId);
		if (flag == false) {
			response.setStatusCode(GameStatusCodeConstant.WORLD_SERVER_NOT_EXSIT);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = CrossBattleCmd.EXCHANGE_POINT)
	public void exchangePoint(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		boolean flag = worldClientSession.game2WorldForward(response, actorId, bytes);
		if (flag == false) {
			response.setStatusCode(GameStatusCodeConstant.WORLD_SERVER_NOT_EXSIT);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = CrossBattleCmd.GET_CROSS_BATTLE_CONFIG)
	public void getCrossBattleConfig(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		boolean flag = worldClientSession.game2WorldForward(response, actorId);
		if (flag == false) {
			response.setStatusCode(GameStatusCodeConstant.WORLD_SERVER_NOT_EXSIT);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = CrossBattleCmd.GET_LAST_BATTLE_RESULT)
	public void getlastBattleResult(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		boolean flag = worldClientSession.game2WorldForward(response, actorId);
		if (flag == false) {
			response.setStatusCode(GameStatusCodeConstant.WORLD_SERVER_NOT_EXSIT);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = CrossBattleCmd.GET_ACTOR_POINT)
	public void getActorPoint(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		boolean flag = worldClientSession.game2WorldForward(response, actorId);
		if (flag == false) {
			response.setStatusCode(GameStatusCodeConstant.WORLD_SERVER_NOT_EXSIT);
			sessionWrite(session, response);
		}
	}
	@Cmd(Id = CrossBattleCmd.SET_READ_FLAG)
	public void getReadFlag(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		boolean flag = worldClientSession.game2WorldForward(response, actorId);
		if (flag == false) {
			response.setStatusCode(GameStatusCodeConstant.WORLD_SERVER_NOT_EXSIT);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = CrossBattleCmd.GET_ALL_END_REWARD)
	public void getReward(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<List<RewardObject>> result = crossBattleRewardFacade.getReward(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			CrossBattleRewardResponse rewardResponse = new CrossBattleRewardResponse(result.item);
			response.setValue(rewardResponse.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = CrossBattleCmd.IS_GET_ALL_EN_REWARD)
	public void isGetReward(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<Integer> result = crossBattleRewardFacade.isGet(actorId);
		CrossBattleIsRewardResponse rewardResponse = new CrossBattleIsRewardResponse(result.item);
		response.setValue(rewardResponse.getBytes());
		sessionWrite(session, response);
	}

}
