package com.jtang.gameserver.module.extapp.deitydesc.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.extapp.deitydesc.handler.DeityDescendCmd;
import com.jtang.gameserver.module.extapp.deitydesc.handler.response.DeityDescendStatusResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class DeityDescendPushHelper {
	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<DeityDescendPushHelper> ref = new ObjectReference<DeityDescendPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static DeityDescendPushHelper getInstance() {
		return ref.get();
	}
	
	public static void pushDeityDescendStatus(long actorId, byte status){
		DeityDescendStatusResponse response = new DeityDescendStatusResponse(status);
		Response rsp = Response.valueOf(ModuleName.DEITY_DESCEND, DeityDescendCmd.STATUS, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
}
