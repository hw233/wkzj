package com.jtang.gameserver.module.adventures.shop.vipshop.handler.response;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

public class VipShopResponse extends IoBufferSerializer {

	/**
	 * 今天已购买信息
	 */
	public Map<Integer,Integer> dayMap = new HashMap<>();
	
	/**
	 * 所有购买信息
	 */
	public Map<Integer,Integer> allMap = new HashMap<>();
	
	public VipShopResponse(Map<Integer,Integer> dayMap,Map<Integer,Integer> allMap){
		this.dayMap = dayMap;
		this.allMap = allMap;
	}
	
	@Override
	public void write() {
		writeIntMap(dayMap);
		writeIntMap(allMap);
	}
}
