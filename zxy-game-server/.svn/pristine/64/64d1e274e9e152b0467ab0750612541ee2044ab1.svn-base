package com.jtang.gameserver.module.love.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class LoveShopRequest extends IoBufferSerializer {

	
	/**
	 * 商品id
	 * */
	public int shopId;
	
	/**
	 * 购买的数量
	 * @param bytes
	 */
	public int num;
	
	public LoveShopRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		shopId = readInt();
		num = readInt();
	}
}
