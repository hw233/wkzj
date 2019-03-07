package com.jtang.gameserver.module.adventures.shop.trader.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.shop.trader.type.ShopType;

public class ShopInfoRequest extends IoBufferSerializer {

	/**
	 * 商店类型
	 */
	public ShopType shopType;
	
	public ShopInfoRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.shopType = ShopType.getType(readInt());
	}
}
