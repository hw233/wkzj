package com.jtang.gameserver.module.snatch.handler;

import static com.jiatang.common.GameStatusCodeConstant.SNATCH_BACK_FAIL;
import static com.jiatang.common.GameStatusCodeConstant.SNATCH_ENEMY_NOT_IN_LIST;
import static com.jiatang.common.GameStatusCodeConstant.SNATCH_TARGET_NOT_FOUND;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Snatch;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.snatch.facade.SnatchEnemyFacade;
import com.jtang.gameserver.module.snatch.facade.SnatchFacade;
import com.jtang.gameserver.module.snatch.handler.request.ExchangeRequest;
import com.jtang.gameserver.module.snatch.handler.request.StartSnatchRequest;
import com.jtang.gameserver.module.snatch.handler.response.SnatchEnemysResponse;
import com.jtang.gameserver.module.snatch.handler.response.SnatchInfoResponse;
import com.jtang.gameserver.module.snatch.handler.response.SnatchNumResponse;
import com.jtang.gameserver.module.snatch.model.SnatchEnemyVO;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class SnatchHandler  extends GatewayRouterHandlerImpl{

	@Autowired
	SnatchFacade snatchFacade;
	@Autowired
	SnatchEnemyFacade snatchEnemyFacade;
	@Autowired
	NotifyFacade notifyFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.SNATCH;
	}

	@Cmd(Id=SnatchCmd.SNATCH_BASE_INFO)
	public void getSnatchInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Snatch snatch = snatchFacade.get(actorId);
		List<SnatchEnemyVO> list = snatchEnemyFacade.getEnemyList(actorId, false);
		SnatchInfoResponse packet = new SnatchInfoResponse(snatch,list);
		sessionWrite(session, response, packet);
	}
	
	@Cmd(Id=SnatchCmd.START_SNATCH)
	public void startSnatch(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		StartSnatchRequest request = new StartSnatchRequest(bytes);
		if (actorId == request.targetActorId) {
			response.setStatusCode(SNATCH_TARGET_NOT_FOUND);
			sessionWrite(session, response);
			return;
		}
		
//		boolean allowSnatchBack = false;
		
		if(request.notifyId > 0) {
//			allowSnatchBack = true;
			boolean canSnatch = notifyFacade.ableSnatch(actorId, request.targetActorId, request.notifyId);
			if (canSnatch == false) {
				response.setStatusCode(SNATCH_BACK_FAIL);
				sessionWrite(session, response);
				return;
			}
		}else{
			SnatchEnemyVO vo = snatchEnemyFacade.getEnemy(actorId, request.targetActorId);
			if(vo == null) {
				response.setStatusCode(SNATCH_ENEMY_NOT_IN_LIST);
				sessionWrite(session, response);
				return;
			}
		}
		
		Result result = snatchFacade.startSnatch(actorId, request.targetActorId, request.notifyId);
		if(result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id=SnatchCmd.SNATCH_CHANGE_ENEMY)
	public void snatchChangeEnemy(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		List<SnatchEnemyVO> list = snatchEnemyFacade.getEnemyList(actorId, true);
		SnatchEnemysResponse packet = new SnatchEnemysResponse(list);
		sessionWrite(session, response, packet);
	}
	
	@Cmd(Id=SnatchCmd.EXCHANGE)
	public void exchange(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		ExchangeRequest request = new ExchangeRequest(bytes);
		Result result = this.snatchFacade.exchange(actorId, request.type, request.exchangeId,request.num);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id=SnatchCmd.GET_EXCHANGE_LIST)
	public void getExchangeList(IoSession session, byte[] bytes, Response response) {
//		long actorId = playerSession.getActorId(session);
//		TResult<ExchangeListResponse> result = this.snatchFacade.getExchangeList(actorId);
//		if(result.isFail()){
//			response.setStatusCode(result.statusCode);
//			sessionWrite(session, response);
//		}else{
//			response.setValue(result.item.getBytes());
//			sessionWrite(session, response);
//		}
	}
	
	@Cmd(Id=SnatchCmd.FLUSH_EXCHANGE)
	public void flushExchange(IoSession session, byte[] bytes, Response response) {
		/*
		long actorId = playerSession.getActorId(session);
		TResult<ReflushExchangeResponse> result = this.snatchFacade.flushExchange(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			response.setValue(result.item.getBytes());
			sessionWrite(session, response);
		}
		*/
	}
	
	@Cmd(Id = SnatchCmd.BUY_SNATCH_NUM)
	public void buySnatchNum(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<SnatchNumResponse> result = snatchFacade.buySnatchNum(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
}
