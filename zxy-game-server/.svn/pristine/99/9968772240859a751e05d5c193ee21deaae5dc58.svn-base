package com.jtang.gameserver.module.sprintgift.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.module.sprintgift.facade.SprintGiftFacade;
import com.jtang.gameserver.module.sprintgift.handler.request.ReceiveSpecifySprintGiftRequest;
import com.jtang.gameserver.module.sprintgift.handler.response.SprintGiftStatusListResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;


/**
 * 等级礼包消息处理
 * @author ligang
 */
@Component
public class SprintGiftHandler extends GatewayRouterHandlerImpl {

	@Autowired
	SprintGiftFacade sprintGiftFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.SPRINT_GIFT;
	}

	@Cmd(Id=SprintGiftCmd.GET_SPRINT_GIFT_LIST)
	public void getSprintGiftStatusList(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		SprintGiftStatusListResponse sprintGiftStatusListResponse = new SprintGiftStatusListResponse();
		sprintGiftStatusListResponse.levelStatusMap = sprintGiftFacade.getSprintGiftStatusList(actorId);
		sessionWrite(session, response, sprintGiftStatusListResponse);
	}
	
	
	@Cmd(Id=SprintGiftCmd.GET_SPECIFY_SPRINT_GIFT)
	public void receiveSprintGift(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		ReceiveSpecifySprintGiftRequest request = new ReceiveSpecifySprintGiftRequest(bytes);
		Result result = sprintGiftFacade.receiveGift(actorId, request.specifyLevel);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
}
