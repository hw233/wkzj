package com.jtang.gameserver.module.adventures.shop.vipshop.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class VipShopBuyRequest extends IoBufferSerializer {

	/**
	 * 商品id
	 */
	public int id;
	
	/**
	 * 数量
	 */
	public int num;
	
	public VipShopBuyRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.id = readInt();
		this.num = readInt();
	}
}
