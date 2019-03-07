package com.jtang.gameserver.module.ladder.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.ladder.facade.LadderFacade;
import com.jtang.gameserver.module.ladder.handler.request.FightVideoRequest;
import com.jtang.gameserver.module.ladder.handler.request.LadderFightRequest;
import com.jtang.gameserver.module.ladder.handler.response.BuyFightNumResponse;
import com.jtang.gameserver.module.ladder.handler.response.FightVideoResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderActorResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderFightInfoResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderRankResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderRewardResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class LadderHandler extends GatewayRouterHandlerImpl {

	@Autowired
	private LadderFacade ladderFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.LADDER;
	}

	
	@Cmd(Id=LadderCmd.GET_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<LadderResponse> result = ladderFacade.getInfo(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = LadderCmd.START_FIGHT)
	public void startFight(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		LadderFightRequest request = new LadderFightRequest(bytes);
		Result result = ladderFacade.startFight(actorId,request.targetActorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = LadderCmd.FLUSH_ACTOR)
	public void flushActor(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<LadderActorResponse> result = ladderFacade.flushActor(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = LadderCmd.LADDER_RANK)
	public void getRank(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<LadderRankResponse> result = ladderFacade.getRank(actorId);
		sessionWrite(session, response,result.item);
	}
	
	@Cmd(Id = LadderCmd.LADDER_REWARD)
	public void ladderReward(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<LadderRewardResponse> result = ladderFacade.getLadderReward(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	
	@Cmd(Id = LadderCmd.BUY_FIGHT_NUM)
	public void buyFightNum(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<BuyFightNumResponse> result = ladderFacade.buyFightNum(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = LadderCmd.LADDER_FIGHT_INFO)
	public void getFightInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<LadderFightInfoResponse> result = ladderFacade.getFightInfo(actorId);
		sessionWrite(session, response,result.item);
	}
	
	@Cmd(Id = LadderCmd.GET_FIGHT_VIDEO)
	public void getFightVideo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		FightVideoRequest request = new FightVideoRequest(bytes);
		TResult<FightVideoResponse> result = ladderFacade.getFightVideo(actorId,request.fightVideoId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			sessionWrite(session, response,result.item);
		}
	}
}
