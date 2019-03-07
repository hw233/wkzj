package com.jtang.gameserver.module.equip.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 删除装备消息
 * @author liujian
 *
 */
public class EquipDelResponse extends IoBufferSerializer {

	/**
	 * 装备唯一id
	 */
	public long uuid;
	
	public EquipDelResponse(long uuid) {
		this.uuid = uuid;
	}

	@Override
	public void write() {
		writeLong(uuid);
	}
	
}
