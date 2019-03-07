package com.jtang.gameserver.admin.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.RechargeFacade;
import com.jtang.gameserver.admin.handler.request.DecreaseTicketRequest;
import com.jtang.gameserver.admin.handler.request.GiveTicketRequest;
import com.jtang.gameserver.admin.handler.request.RechargeRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

@Component
public class RechargeHandler extends AdminRouterHandlerImpl {

	@Autowired
	private RechargeFacade rechargeFacade;
	@Override
	public byte getModule() {
		return GameAdminModule.RECHARGE;
	}
	
	@Cmd(Id = RechargeCmd.RECHARGE)
	public void recharge(IoSession session, byte[] bytes, Response response) {
		RechargeRequest rechargeRequest = RechargeRequest.valueOf(bytes);
		Result result = rechargeFacade.recharge(rechargeRequest.orderSnid, rechargeRequest.platformId, rechargeRequest.serverId, rechargeRequest.uid,
				rechargeRequest.payWayId, rechargeRequest.tradeSnid, rechargeRequest.rechargeId, rechargeRequest.discount, rechargeRequest.payMoney,
				rechargeRequest.buyCount, rechargeRequest.sendCount, rechargeRequest.mediaId, rechargeRequest.channelId, rechargeRequest.rechargeTime,rechargeRequest.actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = RechargeCmd.GIVE)
	public void giveTicket(IoSession session, byte[] bytes, Response response) {
		GiveTicketRequest giveTicketRequest = new GiveTicketRequest(bytes);
		Result result = rechargeFacade.giveTicket(giveTicketRequest.actorId, giveTicketRequest.giveNum);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = RechargeCmd.DECREASE_TICKET)
	public void decreaseTicket(IoSession session, byte[] bytes, Response response) {
		DecreaseTicketRequest dicreaseTicketRequest = new DecreaseTicketRequest(bytes);
		Result result = rechargeFacade.dicreaseTicket(dicreaseTicketRequest.actorId, dicreaseTicketRequest.ticket);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	

}
