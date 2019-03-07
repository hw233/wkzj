package com.jtang.gameserver.module.delve.handler;

import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.delve.facade.DelveFacade;
import com.jtang.gameserver.module.delve.handler.request.DoDelveRequest;
import com.jtang.gameserver.module.delve.handler.request.LastDelveRequest;
import com.jtang.gameserver.module.delve.handler.request.OneKeyDelveRequest;
import com.jtang.gameserver.module.delve.handler.request.RepeatDelveRequest;
import com.jtang.gameserver.module.delve.handler.response.DoDelveResponse;
import com.jtang.gameserver.module.delve.handler.response.LastDelveResponse;
import com.jtang.gameserver.module.delve.handler.response.OneKeyDelveResponse;
import com.jtang.gameserver.module.delve.model.DelveResult;
import com.jtang.gameserver.module.delve.model.DoDelveResult;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

/**
 * 潜修handler
 * @author 0x737263
 *
 */
@Component
public class DelveHandler extends GatewayRouterHandlerImpl {

	@Autowired
	public DelveFacade delveFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.DELVE;
	}
	
//	/**
//	 * 获取潜修室数据
//	 * @param session
//	 * @param bytes
//	 * @param response
//	 */
//	@Cmd(Id = DelveCmd.GET_DELVE_INFO)
//	public void getDelve(IoSession session, byte[] bytes, Response response){
//		long actorId = playerSession.getActorId(session);
//		DelveVO delveVO = delveFacade.get(actorId);
//		if (delveVO == null){
//			response.setStatusCode(DELVE_INFO_ERROR);
//			sessionWrite(session, response);
//			return;
//		}
//		DelveInfoResponse delveInfoResponse = new DelveInfoResponse(delveVO);
//		sessionWrite(session, response, delveInfoResponse);
//	}
//	
//	/**
//	 * 升级潜修室
//	 * @param session
//	 * @param bytes
//	 * @param response
//	 */
//	@Cmd(Id = DelveCmd.LEVEL_UP)
//	public void levelUpDelve(IoSession session, byte[] bytes, Response response){
//		long actorId = playerSession.getActorId(session);
//		
//		TResult<Integer> code = delveFacade.upgrade(actorId);
//		response.setStatusCode(code.statusCode);
//		sessionWrite(session, response);
//	}
	
	/**
	 * 获取上次潜修属性
	 */
	@Cmd(Id = DelveCmd.LAST_DELVE)
	public void lastDelve(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		LastDelveRequest request = new LastDelveRequest(bytes);
		TResult<LastDelveResponse> result = delveFacade.lastDelve(actorId, request.heroId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			sessionWrite(session, response, result.item);
		}
	}
	
	/**
	 *  潜修仙人
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = DelveCmd.DO_DELVE)
	public void doDelve(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		DoDelveRequest doDelveRequest = new DoDelveRequest(bytes);
		TResult<Map<HeroVOAttributeKey, DoDelveResult>> result = delveFacade.doDelve(actorId, doDelveRequest.heroId, doDelveRequest.type);
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			DoDelveResponse doDelveResponse = new DoDelveResponse(doDelveRequest.heroId, result.item);
			sessionWrite(session, response, doDelveResponse);
		}
	}

	@Cmd(Id = DelveCmd.REPEAT_DELVE)
	public void repeatDelve(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		RepeatDelveRequest request = new RepeatDelveRequest(bytes);
		Result result = delveFacade.repeatDelve(actorId, request.heroId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = DelveCmd.ONE_KEY_DELVE)
	public void oneKeyDelve(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		OneKeyDelveRequest request = new OneKeyDelveRequest(bytes);
		TResult<List<DelveResult>> result = delveFacade.oneKeyDelve(actorId, request.heroId, request.type,request.attribute);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			OneKeyDelveResponse rsp = new OneKeyDelveResponse(result.item);
			sessionWrite(session, response, rsp);
		}
	}
}
