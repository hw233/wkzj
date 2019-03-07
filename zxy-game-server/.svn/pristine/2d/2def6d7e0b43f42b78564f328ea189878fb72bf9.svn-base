package com.jtang.gameserver.module.adventures.shop.trader.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.shop.trader.type.ShopType;

public class ShopRequest extends IoBufferSerializer {
	
	/**
	 * 商店类型
	 */
	public ShopType shopType;

	/**
	 * 商品id
	 * */
	public int shopId;
	
	/**
	 * 购买的数量
	 * @param bytes
	 */
	public int num;
	
	public ShopRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		shopType = ShopType.getType(readInt());
		shopId = readInt();
		num = readInt();
	}
}
