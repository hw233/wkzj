package com.jtang.gameserver.module.cdkey.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.lop.result.ListLopResult;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.gameserver.component.lop.response.CdkeyLOPResponse;
import com.jtang.gameserver.module.cdkey.facade.CdkeyFacade;
import com.jtang.gameserver.module.cdkey.handler.request.CdkeyRequest;
import com.jtang.gameserver.module.cdkey.handler.response.CdkeyResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class CdkeyHandler extends GatewayRouterHandlerImpl {

	@Autowired
	CdkeyFacade cdkeyFacade;

	@Override
	public byte getModule() {
		return ModuleName.CDKEY;
	}

	@Cmd(Id = CdkeyCmd.GET_PACKAGE)
	public void getPackage(IoSession session, byte[] bytes, Response response) {
		CdkeyRequest request = new CdkeyRequest(bytes);
		String cdkey = request.cdkey;
		long actorId = request.actorId;

		ListLopResult<CdkeyLOPResponse> result = cdkeyFacade.getPackage(cdkey, actorId);
		CdkeyResponse packet = new CdkeyResponse(result);
		sessionWrite(session, response, packet);
	}
}
