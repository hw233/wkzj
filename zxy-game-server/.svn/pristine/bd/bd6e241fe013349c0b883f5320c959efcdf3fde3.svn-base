package com.jtang.gameserver.module.praise.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.praise.handler.PraiseCmd;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class PraisePushHelper {
	@Autowired
	private PlayerSession playerSession;
	
	private static ObjectReference<PraisePushHelper> ref = new ObjectReference<PraisePushHelper>();
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	public static void pushActive(long actorId) {
		Response response = Response.valueOf(ModuleName.PRAISE, PraiseCmd.PUSH_ACTIVE);
		ref.get().playerSession.push(actorId, response);
	}
}
