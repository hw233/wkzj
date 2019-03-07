package com.jtang.gameserver.module.extapp.welkin.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.extapp.welkin.handler.WelkinCmd;
import com.jtang.gameserver.module.extapp.welkin.handler.response.WelkinResponse;
import com.jtang.gameserver.module.extapp.welkin.handler.response.WelkinStateResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class WelkinPushHelper {

	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<WelkinPushHelper> ref = new ObjectReference<WelkinPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	private static WelkinPushHelper getInstance() {
		return ref.get();
	}
	
	public static void pushState(Long actorId, int state) {
		WelkinStateResponse stateResponse = new WelkinStateResponse(state);
		Response rsp = Response.valueOf(ModuleName.WELKIN, WelkinCmd.PUSH_STATE,stateResponse.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushWelkinResponse(long actorId,WelkinResponse response){
		Response rsp = Response.valueOf(ModuleName.WELKIN, WelkinCmd.PUSH_WELKIN_INFO,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

}
