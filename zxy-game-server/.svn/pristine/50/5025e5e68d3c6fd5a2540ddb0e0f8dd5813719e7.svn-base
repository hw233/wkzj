package com.jtang.gameserver.module.user.handler.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;

/**
 * 角色属性变化
 * @author 0x737263
 *
 */
public class ActorAttributeResponse extends IoBufferSerializer {

	/**
	 * key:角色属性 value:属性值
	 */
	public Map<ActorAttributeKey, Object> attributeMaps;

	/**
	 * 单个属性推送
	 * 
	 * @param key
	 * @param value
	 */
	public ActorAttributeResponse(ActorAttributeKey key, Object value) {
		attributeMaps = new HashMap<>();
		attributeMaps.put(key, value);
	}

	/**
	 * 多个属性推送
	 * 
	 * @param attributeList
	 */
	public ActorAttributeResponse(Map<ActorAttributeKey, Object> attributeMaps) {
		this.attributeMaps = attributeMaps;
	}

	@Override
	public void write() {
		writeShort((short) attributeMaps.size());
		for (Entry<ActorAttributeKey, Object> entry : attributeMaps.entrySet()) {
			writeByte(entry.getKey().getCode());
			writeObject(entry.getValue());
		}
	}
}
