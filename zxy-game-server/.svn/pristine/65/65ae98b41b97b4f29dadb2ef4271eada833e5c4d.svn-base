package com.jtang.gameserver.module.adventures.shop.shop.helper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.adventures.shop.shop.handler.ShopCmd;
import com.jtang.gameserver.module.adventures.shop.shop.handler.response.ExchangeListResponse;
import com.jtang.gameserver.module.adventures.shop.shop.handler.response.ShopInfoResponse;
import com.jtang.gameserver.module.adventures.shop.shop.model.ShopVO;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class ShopPushHelp {

	@Autowired
	PlayerSession playerSession; 

	private static ObjectReference<ShopPushHelp> REF = new ObjectReference<>();

	@PostConstruct
	protected void init() {
		REF.set(this);
	}

	private static ShopPushHelp getInstance() {
		return REF.get();
	}
	
	/**
	 * 推送购买商品信息
	 * */
	public static void pushShopInfo(long actorId,ShopVO shopVo){
		List<ShopVO> shopVoList=new ArrayList<ShopVO>();
		shopVoList.add(shopVo);
		pushShopInfo(actorId, shopVoList);
	}
	
	/**
	 * 推送购买商品信息集合
	 **/
	public static void pushShopInfo(long actorId, List<ShopVO> shopVoList) {
		ShopInfoResponse shopResponse = new ShopInfoResponse(shopVoList);
		Response response = Response.valueOf(ModuleName.SHOP, ShopCmd.SHOP_BUY_INFO, shopResponse.getBytes());
		getInstance().playerSession.push(actorId, response);
	}

	public static void pushExchangeResponse(Long actorId,ExchangeListResponse response) {
		Response rsp = Response.valueOf(ModuleName.SHOP, ShopCmd.BLACK_SHOP_INFO,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
}
