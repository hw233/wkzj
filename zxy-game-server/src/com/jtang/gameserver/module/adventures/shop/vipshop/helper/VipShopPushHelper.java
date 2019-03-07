package com.jtang.gameserver.module.adventures.shop.vipshop.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.adventures.shop.vipshop.handler.VipShopCmd;
import com.jtang.gameserver.server.broadcast.Broadcast;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class VipShopPushHelper {

	@Autowired
	Broadcast broadcast;
	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<VipShopPushHelper> REF = new ObjectReference<>();

	@PostConstruct
	protected void init() {
		REF.set(this);
	}

	private static VipShopPushHelper getInstance() {
		return REF.get();
	}
	
	/**
	 * 推送跨天
	 **/
	public static void pushReset(long actorId) {
		Response rsp = Response.valueOf(ModuleName.VIP_SHOP, VipShopCmd.PUSH_RESET);
		getInstance().playerSession.push(actorId, rsp);
	}
}
