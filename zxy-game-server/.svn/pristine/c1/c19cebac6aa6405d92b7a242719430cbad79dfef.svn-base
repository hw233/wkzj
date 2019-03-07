package com.jtang.gameserver.module.ally.handler;

import java.util.Collection;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.ally.facade.AllyFacade;
import com.jtang.gameserver.module.ally.handler.request.AllyAddRequest;
import com.jtang.gameserver.module.ally.handler.request.AllyFightRequest;
import com.jtang.gameserver.module.ally.handler.request.AllyRemoveRequest;
import com.jtang.gameserver.module.ally.handler.request.CoordinateUpdateRequest;
import com.jtang.gameserver.module.ally.handler.request.GetCoordinateRequest;
import com.jtang.gameserver.module.ally.handler.response.AllyListResponse;
import com.jtang.gameserver.module.ally.handler.response.CoordinateResponse;
import com.jtang.gameserver.module.ally.handler.response.GetActorsResponse;
import com.jtang.gameserver.module.ally.model.AllyVO;
import com.jtang.gameserver.module.ally.model.CoordinateVO;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
/**
 * 
 * @author pengzy
 *
 */
@Component
public class AllyHandler extends GatewayRouterHandlerImpl{

	@Autowired
	private AllyFacade allyFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.ALLY;
	}

	/**
	 * 角色登录的时候获取好友列表以及今日重置日切磋次数的倒计时以及已经切磋的次数
	 * 重置日切磋次数的处理流程：
	 * 1、当角色登录的时候-若在重置时间之后登录且最近一次切磋在重置时间之前，则必重置；
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = AllyCmd.GET_ALLY_LIST)
	public void getAllyList(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Collection<AllyVO> allyVOList = allyFacade.getAlly(actorId);
		int countDownSeconds = allyFacade.getCountDown(actorId);
		int dayFightCount = allyFacade.getDayFightCount(actorId);
		AllyListResponse allyListResp = new AllyListResponse(allyVOList, countDownSeconds, dayFightCount);
		sessionWrite(session, response, allyListResp);
	}
	
	@Cmd(Id = AllyCmd.ADD_ALLY)
	public void addAlly(IoSession session, byte[] bytes, Response response) {
		AllyAddRequest allyReq = new AllyAddRequest(bytes);
		long actorId = playerSession.getActorId(session);
		Result result = allyFacade.addAlly(actorId, allyReq.allyActorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}

	@Cmd(Id = AllyCmd.REMOVE_ALLY)
	public void removeAlly(IoSession session, byte[] bytes, Response response) {
		AllyRemoveRequest removeAllyReq = new AllyRemoveRequest(bytes);
		long actorId = playerSession.getActorId(session);
		Result result = allyFacade.removeAlly(actorId, removeAllyReq.actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = AllyCmd.ALLY_FIGHT)
	public void allyFight(IoSession session, byte[] bytes, Response response) {
		AllyFightRequest fightRequest = new AllyFightRequest(bytes);
		long actorId = playerSession.getActorId(session);
		Result result = allyFacade.fight(actorId, fightRequest.allyActorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = AllyCmd.GET_COORDINATE)
	public void getCoordinate(IoSession session, byte[] bytes, Response response){
		GetCoordinateRequest request = new GetCoordinateRequest(bytes);
		TResult<CoordinateVO> result = allyFacade.getCoordinate(request.actorId);
		if (result.isFail()) {
			result.item = new CoordinateVO();
			result.item.latitude = 0;
			result.item.longitude = 0;
			result.item.levelError = 0;
		}
		
		int allyNum = allyFacade.getAlly(request.actorId).size();
		int actorLevel = ActorHelper.getActorLevel(request.actorId);
		CoordinateResponse packet = new CoordinateResponse(result.item, allyNum, actorLevel);
		sessionWrite(session, response, packet);
	}
	
	@Cmd(Id = AllyCmd.UPDATE_COORDINATE)
	public void updateCoordinate(IoSession session, byte[] bytes, Response response) {
		CoordinateUpdateRequest request = new CoordinateUpdateRequest(bytes);
		allyFacade.updateCoordinate(playerSession.getActorId(session), request.longitude, request.latitude, request.levelError);
	}
	
	
	@Cmd(Id = AllyCmd.GET_ACTORS)
	public void getActors(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<GetActorsResponse> result = allyFacade.getActors(actorId);
		sessionWrite(session, response,result.item);
	}
	
	@Cmd(Id = AllyCmd.GET_ROBOT)
	public void getRobots(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<GetActorsResponse> result = allyFacade.getRobot(actorId);
		sessionWrite(session, response,result.item);
	}
	
	
}
