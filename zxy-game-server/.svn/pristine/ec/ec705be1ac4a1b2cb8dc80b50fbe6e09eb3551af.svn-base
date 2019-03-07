package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 删除装备请求
 * @author ludd
 *
 */
public class DeleteEquipRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	/**
	 * 装备uuid
	 */
	public long uuid;
	
	public DeleteEquipRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.uuid = readLong();
	}

}
