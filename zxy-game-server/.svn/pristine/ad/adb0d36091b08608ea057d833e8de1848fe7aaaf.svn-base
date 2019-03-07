package com.jtang.gameserver.module.adventures.shop.trader.handler.response;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.shop.trader.type.ShopType;

public class ShopOpenResponse extends IoBufferSerializer {

	/**
	 * 开启信息
	 * key:商店类型
	 * value:0.关闭 1.开启
	 */
	public Map<Integer,Integer> openMap = new HashMap<>();
	
	public ShopOpenResponse(Map<Integer,Integer> openMap){
		this.openMap = openMap;
	}
	
	public ShopOpenResponse(ShopType type,int isOpen){
		this.openMap.put(type.getCode(), isOpen);
	}
	
	@Override
	public void write() {
		writeIntMap(openMap);
	}
}
