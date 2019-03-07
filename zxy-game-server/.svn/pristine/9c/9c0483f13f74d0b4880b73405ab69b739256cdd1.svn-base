package com.jtang.gameserver.module.equip.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 客户端向服务器请求删除装备
 * 服务器回复信息为{@code EquipTotalNumResponse}
 * @author liujian
 *
 */
public class EquipSellRequest extends IoBufferSerializer {

	/**
	 * 装备uuid
	 */
	public long uuid;
	
	public EquipSellRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		uuid = this.readLong();
	}

}
