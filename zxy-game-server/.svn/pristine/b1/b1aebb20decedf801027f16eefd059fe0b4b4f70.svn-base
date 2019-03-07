package com.jtang.gameserver.module.extapp.craftsman.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.dataconfig.service.CraftsmanService;
import com.jtang.gameserver.module.extapp.craftsman.facade.CraftsmanFacade;
import com.jtang.gameserver.module.extapp.craftsman.handler.request.BuildEquipRequest;
import com.jtang.gameserver.module.extapp.craftsman.handler.response.BuildEquipResponse;
import com.jtang.gameserver.module.extapp.craftsman.handler.response.CraftsmanStatusResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class CraftsmanHandler extends GatewayRouterHandlerImpl {

	@Autowired
	CraftsmanFacade craftsmanFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.CRAFTSMAN;
	}
	
	@Cmd(Id = CraftsmanCmd.BUILD)
	public void build(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		BuildEquipRequest request = new BuildEquipRequest(bytes);
		TResult<BuildEquipResponse> result = craftsmanFacade.build(actorId, request.uuid, request.buildId, request.useTicket == 2);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			sessionWrite(session, response, result.item);
		}
	}
	
	@Cmd(Id = CraftsmanCmd.STATUS)
	public void status(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Long openTime = CraftsmanService.CRAFTSMAN_GLOBAL_CONFIG.openDateTime.getTime() / 1000;
		Long endTime = CraftsmanService.CRAFTSMAN_GLOBAL_CONFIG.closeDateTime.getTime() / 1000;
		int maybe = 0;
		int buildNum = CraftsmanService.getCraftsmanGlobalConfig().buildCount;
		if (DateUtils.isActiveTime(openTime.intValue(), endTime.intValue())) {
			int now = DateUtils.getNowInSecondes();
			maybe = endTime.intValue() - now;
			buildNum = buildNum - craftsmanFacade.getBuildNum(actorId);
		}
		CraftsmanStatusResponse statusResponse = new CraftsmanStatusResponse(maybe, buildNum);
		sessionWrite(session, response, statusResponse);
	}
}
