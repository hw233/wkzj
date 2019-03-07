package com.jtang.gameserver.module.extapp.rechargeapp.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.extapp.rechargeapp.handler.RechargeAppCmd;
import com.jtang.gameserver.module.extapp.rechargeapp.handler.response.RecharegeAppResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class RechargeAppPushHelper {

	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<RechargeAppPushHelper> ref = new ObjectReference<RechargeAppPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static RechargeAppPushHelper getInstance() {
		return ref.get();
	}

	public static void pushPlantOpen(long actorId,RecharegeAppResponse response){
		Response rsp = Response.valueOf(ModuleName.RECHARGE_APP, RechargeAppCmd.RECHARGE_APP_INFO,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

}
