package com.jtang.gameserver.module.equipdevelop.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.equipdevelop.facade.EquipDevelopFacade;
import com.jtang.gameserver.module.equipdevelop.handler.request.EquipConvertRequest;
import com.jtang.gameserver.module.equipdevelop.handler.request.EquipDevelopRequest;
import com.jtang.gameserver.module.equipdevelop.handler.response.EquipDevelopResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class EquipDevelopHandler extends GatewayRouterHandlerImpl{

	@Autowired
	public EquipDevelopFacade equipDevelopFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.EQUIP_DEVELOP;
	}

	/**
	 * 装备、装备碎片提炼
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = EquipDevelopCmd.EQUIP_CONVERT)
	public void equipConvert(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		EquipConvertRequest request = new EquipConvertRequest(bytes);
		short statusCode = equipDevelopFacade.equipConvert(actorId, request);
		response.setStatusCode(statusCode);
		sessionWrite(session, response);
	}
	
	/**
	 * 装备突破
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = EquipDevelopCmd.EQUIP_DEVELOP)
	public void equipDevelop(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		EquipDevelopRequest request = new EquipDevelopRequest(bytes);
		TResult<EquipDevelopResponse> result = equipDevelopFacade.equipDevelop(actorId, request.getUuid());
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}
		sessionWrite(session, response, result.item);
	}
}
