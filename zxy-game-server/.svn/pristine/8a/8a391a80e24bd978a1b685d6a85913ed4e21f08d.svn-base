package com.jtang.gameserver.module.equip.handler.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.equip.type.EquipAttributeKey;

/**
 * 更新装备属性
 * @author liujian
 *
 */
public class EquipAttributeResponse extends IoBufferSerializer {
	
	/**
	 * 装备uuid
	 */
	public long uuid;
	
	/**
	 * 变更的属性
	 */
	public Map<EquipAttributeKey, Number> attributeMaps;
	
	public EquipAttributeResponse(long uuid,Map<EquipAttributeKey,Number> attributeMaps) {
		this.uuid = uuid;
		this.attributeMaps = attributeMaps;
	}
	
	public EquipAttributeResponse(long uuid, EquipAttributeKey key, int value) {
		this.uuid = uuid;
		this.attributeMaps = new HashMap<>();
		this.attributeMaps.put(key, value);
	}

	@Override
	public void write() {
		writeLong(this.uuid);
		writeShort((short) this.attributeMaps.size());
		for (Entry<EquipAttributeKey, Number> key : this.attributeMaps.entrySet()) {
			writeByte(key.getKey().getCode());
			writeObject(key.getValue());
		}
	}
}
