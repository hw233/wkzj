package com.jtang.gameserver.module.adventures.vipactivity.handler.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;

public class VipActivityResponse extends IoBufferSerializer {

	/**
	 * 奇遇信息
	 */
	Map<Integer,Integer> vipActivity = new HashMap<>();
	
	public VipActivityResponse(Integer key,int value){
		vipActivity.put(key, value);
	}
	
	@Override
	public void write() {
		writeShort((short) vipActivity.size());
		for(Entry<Integer,Integer> entry:vipActivity.entrySet()){
			writeInt(entry.getKey());
			writeInt(entry.getValue());
		}
	}
}
