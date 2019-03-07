package com.jtang.gameserver.module.sign.helper;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.sign.handler.SignCmd;
import com.jtang.gameserver.module.sign.handler.response.SignInfoResponse;
import com.jtang.gameserver.server.session.PlayerSession;
@Component
public class SignPushHelper {
	@Autowired
	private PlayerSession playerSession;
	
	private static ObjectReference<SignPushHelper> ref = new ObjectReference<SignPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static SignPushHelper getInstance() {
		return ref.get();
	}

	public static void pushSignInfo(long actorId, TResult<SignInfoResponse> info) {
		Response response = Response.valueOf(ModuleName.SIGN, SignCmd.SIGN_INFO,info.item.getBytes());
		getInstance().playerSession.push(actorId, response);
	}

}
