package com.jtang.gameserver.module.app.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.app.handler.AppCmd;
import com.jtang.gameserver.module.app.handler.response.GetAppGlobalResponse;
import com.jtang.gameserver.module.app.handler.response.GetAppRecordResponse;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class AppPushHelper {


	@Autowired
	PlayerSession playerSession;
	
	private static ObjectReference<AppPushHelper> ref = new ObjectReference<AppPushHelper>();
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	public static void pushAppRecord(long actorId, AppRecordVO appRecordVO) {
		GetAppRecordResponse packet = new GetAppRecordResponse(appRecordVO);
		Response response = Response.valueOf(ModuleName.APP, AppCmd.PUSH_APP_RECORD, packet.getBytes());
		ref.get().playerSession.push(actorId, response);
	}
	
	public static void pushAppGlobal(long actorId, AppGlobalVO appGlobalVO) {
		GetAppGlobalResponse packet = new GetAppGlobalResponse(appGlobalVO);
		Response response = Response.valueOf(ModuleName.APP, AppCmd.GET_APP_GLOBAL, packet.getBytes());
		ref.get().playerSession.push(actorId, response);
	}

}
