package com.jtang.gameserver.module.delve.handler.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.delve.type.DelveAttributeKey;

public class DelveAttributeResponse extends IoBufferSerializer {

	/**
	 * key:潜修室属性  value:属性值
	 */
	public Map<DelveAttributeKey, Integer> attributeList = new HashMap<DelveAttributeKey, Integer>();
	
	/**
	 * 添加属性
	 * @param delveAttributeKey
	 * @param value
	 */
	public void push(DelveAttributeKey delveAttributeKey, int value){
		attributeList.put(delveAttributeKey, value);
	}
	
	/**
	 * 添加多个属性
	 * @param attributes
	 */
	public void pushMuti(Map<DelveAttributeKey, Integer> attributes){
		for (DelveAttributeKey key : attributeList.keySet()) {
			attributeList.put(key, attributes.get(key));
		}
	}


	@Override
	public void write() {
		writeShort((short) attributeList.size());
		for (Entry<DelveAttributeKey, Integer> entry : attributeList.entrySet()) {
			writeByte(entry.getKey().getCode());
			writeInt(entry.getValue());
		}
	}

}
