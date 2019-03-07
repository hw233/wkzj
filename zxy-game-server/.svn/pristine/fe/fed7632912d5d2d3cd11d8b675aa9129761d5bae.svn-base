package com.jtang.gameserver.module.adventures.shop.shop.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.shop.shop.model.ShopVO;

public class ShopInfoResponse extends IoBufferSerializer  {

	public List<ShopVO> shops;
	
	public ShopInfoResponse(List<ShopVO> shops){
		this.shops=shops;
	}
	
	@Override
	public void write() {
		writeShort((short)shops.size());
		for(ShopVO shop:shops){
			shop.writePacket(this);
		}
	}

}
