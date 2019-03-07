package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 添加装备请求协议
 * @author ludd
 *
 */
public class GiveEquipRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 装备id
	 */
	public int equipId;
	
	public GiveEquipRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = this.readLong();
		this.equipId = readInt();
	}

}
