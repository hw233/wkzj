package com.jtang.gameserver.module.equipdevelop.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 装备突破请求参数
 * @author hezh
 *
 */
public class EquipDevelopRequest extends IoBufferSerializer {

	/** 装备uuid*/
	private long uuid;
	
	public EquipDevelopRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		uuid = this.readLong();
	}

	/**
	 * @return the uuid
	 */
	public long getUuid() {
		return uuid;
	}
}
