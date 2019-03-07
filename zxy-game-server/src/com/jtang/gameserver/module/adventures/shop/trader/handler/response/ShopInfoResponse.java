package com.jtang.gameserver.module.adventures.shop.trader.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.shop.trader.type.ShopType;

public class ShopInfoResponse extends IoBufferSerializer {
	
	/**
	 * 商店类型
	 */
	public int shopType;
	
	public ShopInfoResponse(ShopType shopType){
		this.shopType = shopType.getCode();
	}
	
	@Override
	public void write() {
		writeInt(shopType);
	}
}
