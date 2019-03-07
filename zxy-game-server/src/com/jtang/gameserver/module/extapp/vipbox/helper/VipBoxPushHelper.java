package com.jtang.gameserver.module.extapp.vipbox.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.extapp.vipbox.handler.VipBoxCmd;
import com.jtang.gameserver.module.extapp.vipbox.handler.response.VipBoxResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class VipBoxPushHelper {

	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<VipBoxPushHelper> ref = new ObjectReference<VipBoxPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static VipBoxPushHelper getInstance() {
		return ref.get();
	}
	
	public static void pushBoxResponse(long actorId,VipBoxResponse response){
		Response rsp = Response.valueOf(ModuleName.VIP_BOX, VipBoxCmd.PUSH_BOX,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
}
