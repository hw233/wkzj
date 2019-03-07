package com.jtang.gameserver.module.extapp.deitydesc.handler;

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
import com.jtang.gameserver.dataconfig.service.DeityDescendService;
import com.jtang.gameserver.module.extapp.deitydesc.facade.DeityDescendFacade;
import com.jtang.gameserver.module.extapp.deitydesc.handler.request.DeityDescendHitRequest;
import com.jtang.gameserver.module.extapp.deitydesc.handler.response.DeityDescendHitResponse;
import com.jtang.gameserver.module.extapp.deitydesc.handler.response.DeityDescendInfoResponse;
import com.jtang.gameserver.module.extapp.deitydesc.handler.response.DeityDescendStatusResponse;
import com.jtang.gameserver.module.extapp.deitydesc.model.DeityDescendVO;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class DeityDescendHandler extends GatewayRouterHandlerImpl {

	@Autowired
	DeityDescendFacade deityDescendFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.DEITY_DESCEND;
	}
	
	@Cmd(Id = DeityDescendCmd.GET_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<DeityDescendVO> result = deityDescendFacade.getDeityDescendInfo(actorId);
		if (result.isOk()) {
			DeityDescendInfoResponse resultResponse = new DeityDescendInfoResponse(result.item.heroId, result.item.curIndex, DeityDescendService.getEndDate());
			sessionWrite(session, response, resultResponse);
		} else {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = DeityDescendCmd.HIT)
	public void hit(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		DeityDescendHitRequest deityDescendHitRequest = new DeityDescendHitRequest(bytes);
		TResult<List<RewardObject>> result = deityDescendFacade.hitDeityDescend(actorId, deityDescendHitRequest.hitCount);
		if(result.isOk()){
			DeityDescendVO vo = deityDescendFacade.getCurDeityDescendVO(actorId);
			DeityDescendHitResponse resultResponse = new DeityDescendHitResponse(vo.curIndex, result.item);
			sessionWrite(session, response, resultResponse);
		}else{
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = DeityDescendCmd.STATUS)
	public void getStatus(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Result result = deityDescendFacade.getStatus(actorId);
		byte status = -1;
		if (result.isFail()) {
			status = 0;
		} else {
			status = 1;
		}
		DeityDescendStatusResponse resultResponse = new DeityDescendStatusResponse(status);
		sessionWrite(session, response, resultResponse);
	}
	
	@Cmd(Id = DeityDescendCmd.RECEIVE)
	public void receiveHero(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Result result = deityDescendFacade.receiveHero(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}
		sessionWrite(session, response);
	}
	
}
