package com.jtang.gameserver.module.refine.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.refine.facade.RefineFacade;
import com.jtang.gameserver.module.refine.handler.request.RefineEquipRequest;
import com.jtang.gameserver.module.refine.handler.response.RefineEquipResponse;
import com.jtang.gameserver.module.refine.model.RefineResult;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

/**
 * 精炼室handler
 * @author 0x737263
 *
 */
@Component
public class RefineHandler extends GatewayRouterHandlerImpl {

	@Autowired
	public RefineFacade refineFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.REFINE;
	}

//	/**
//	 * 获取精炼信息
//	 * @param session
//	 * @param bytes
//	 * @param response
//	 */
//	@Cmd(Id = RefineCmd.REFINE_INFO)
//	public void getInfo(IoSession session, byte[] bytes, Response response) {
//		long actorId = playerSession.getActorId(session);
//		Refine refine = refineFacade.get(actorId);
//		RefineInfoReponse packet = new RefineInfoReponse(refine.level);
//		sessionWrite(session, response, packet);
//	}
//	
//	/**
//	 * 升级精炼室
//	 * @param session
//	 * @param bytes
//	 * @param response
//	 */
//	@Cmd(Id = RefineCmd.UPGRADE)
//	public void upgrade(IoSession session, byte[] bytes, Response response) {
//		long actorId = playerSession.getActorId(session);
//		TResult<Integer> result = refineFacade.upgrade(actorId);
//		if(result.isFail())  {
//			response.setStatusCode(result.statusCode);
//			sessionWrite(session, response);
//			return;
//		}
//		
//		RefineInfoReponse packet = new RefineInfoReponse(result.item);
//		sessionWrite(session, response, packet);
//	}
	
	/**
	 * 精炼装备
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = RefineCmd.REFINE)
	public void refine(IoSession session, byte[] bytes, Response response) {
		RefineEquipRequest request = new RefineEquipRequest(bytes);
		long actorId = playerSession.getActorId(session);
		TResult<RefineResult> result = refineFacade.refineEquip(actorId, request.uuid, request.refineType,request.refineNum);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}
		RefineEquipResponse packet = new RefineEquipResponse(result);
		sessionWrite(session, response, packet);
	}
	
}
