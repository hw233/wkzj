package com.jtang.gameserver.module.adventures.shop.trader.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.adventures.shop.trader.handler.TraderCmd;
import com.jtang.gameserver.module.adventures.shop.trader.handler.response.ShopInfoResponse;
import com.jtang.gameserver.module.adventures.shop.trader.handler.response.ShopOpenResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class TraderPushHelper {
	
	@Autowired
	PlayerSession playerSession; 

	private static ObjectReference<TraderPushHelper> REF = new ObjectReference<>();

	@PostConstruct
	protected void init() {
		REF.set(this);
	}

	private static TraderPushHelper getInstance() {
		return REF.get();
	}

	public static void pushTraderOpen(long actorId, ShopInfoResponse item) {
		Response response = Response.valueOf(ModuleName.TRADER, TraderCmd.PUSH_DURATION, item.getBytes());
		getInstance().playerSession.push(actorId, response);
	}
	
	public static void pushTraderInfo(long actorId, ShopInfoResponse item) {
		Response response = Response.valueOf(ModuleName.TRADER, TraderCmd.TRADER_INFO, item.getBytes());
		getInstance().playerSession.push(actorId, response);
	}
	
	/**
	 * 推送商店状态变更
	 */
	public static void pushShopState(long actorId,ShopOpenResponse response){
		Response rsp = Response.valueOf(ModuleName.TRADER, TraderCmd.PUSH_SHOP_STATE,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

}
