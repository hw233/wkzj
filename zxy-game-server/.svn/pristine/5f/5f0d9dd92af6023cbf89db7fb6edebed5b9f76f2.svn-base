package com.jtang.gameserver.module.extapp.rechargeapp.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.rechargeapp.facade.RechargeAppFacade;
import com.jtang.gameserver.module.extapp.rechargeapp.handler.response.RecharegeAppResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class RechargeAppHandler extends GatewayRouterHandlerImpl {

	@Autowired
	RechargeAppFacade rechargeAppFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.RECHARGE_APP;
	}
	
	@Cmd(Id = RechargeAppCmd.RECHARGE_APP_INFO)
	public void getRecharge(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<RecharegeAppResponse> result = rechargeAppFacade.getRecharege(actorId);
		sessionWrite(session, response, result.item);
	}
}
