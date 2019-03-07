package com.jtang.gameserver.module.extapp.craftsman.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.extapp.craftsman.handler.CraftsmanCmd;
import com.jtang.gameserver.module.extapp.craftsman.handler.response.CraftsmanStatusResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class CraftsmanPushHelper {
	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<CraftsmanPushHelper> ref = new ObjectReference<CraftsmanPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static CraftsmanPushHelper getInstance() {
		return ref.get();
	}
	
	public static void pushCraftsmanStatus(long actorId, int time, int buildNum){
		CraftsmanStatusResponse response = new CraftsmanStatusResponse(time, buildNum);
		Response rsp = Response.valueOf(ModuleName.CRAFTSMAN, CraftsmanCmd.STATUS, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
}
