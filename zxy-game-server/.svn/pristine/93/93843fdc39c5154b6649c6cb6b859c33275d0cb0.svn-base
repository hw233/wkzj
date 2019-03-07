package com.jtang.gameserver.module.extapp.onlinegifts.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.extapp.onlinegifts.handler.OnlineGiftsCmd;
import com.jtang.gameserver.module.extapp.onlinegifts.handler.response.OnlineGiftsInfoResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class OnlineGiftsPushHelper {
	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<OnlineGiftsPushHelper> ref = new ObjectReference<OnlineGiftsPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static OnlineGiftsPushHelper getInstance() {
		return ref.get();
	}
	
	
	/**
	 * 
	 * @param actorId 接受者
	 * @param response 相应
	 */
	public static void pushOnlineGiftsInfo(long actorId, OnlineGiftsInfoResponse response){
		Response rsp = Response.valueOf(ModuleName.ONLINEGIFTS, OnlineGiftsCmd.ONLINE_INFO, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
}
