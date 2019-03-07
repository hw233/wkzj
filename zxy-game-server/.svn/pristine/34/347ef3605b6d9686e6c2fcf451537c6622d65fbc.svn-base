package com.jtang.gameserver.module.extapp.ernie.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.extapp.ernie.handler.ErnieCmd;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieInfoResponse;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieStatusResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class ErniePushHelper {
	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<ErniePushHelper> ref = new ObjectReference<ErniePushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static ErniePushHelper getInstance() {
		return ref.get();
	}
	
	public static void pushErnieStatus(long actorId, ErnieStatusResponse response) {
		Response rsp = Response.valueOf(ModuleName.ERNIE, ErnieCmd.STATUS, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushErnieInfo(long actorId, ErnieInfoResponse response) {
		Response rsp = Response.valueOf(ModuleName.ERNIE, ErnieCmd.ERNIE_INFO, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
}
