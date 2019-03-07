package com.jtang.gameserver.module.extapp.beast.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.beast.facade.BeastFacade;
import com.jtang.gameserver.module.extapp.beast.handler.request.BeastAttackRequest;
import com.jtang.gameserver.module.extapp.beast.handler.response.BeastInfoResponse;
import com.jtang.gameserver.module.extapp.beast.handler.response.BeastStatusResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class BeastHandler extends GatewayRouterHandlerImpl {

	@Autowired
	private BeastFacade beastFacade;

	@Override
	public byte getModule() {
		return ModuleName.BEAST;
	}

	@Cmd(Id = BeastCmd.STATUS)
	public void getStatus(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<BeastStatusResponse> result = beastFacade.getStatus(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}

		sessionWrite(session, response, result.item);
	}

	@Cmd(Id = BeastCmd.INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<BeastInfoResponse> result = beastFacade.getInfo(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}

		sessionWrite(session, response, result.item);
	}

	@Cmd(Id = BeastCmd.ACTTACK)
	public void attackBoss(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		BeastAttackRequest attackBossRequest = new BeastAttackRequest(bytes);
		boolean useTicket = attackBossRequest.useTicket == 1 ? true : false;
		Result result = beastFacade.attack(actorId, useTicket);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}

}
