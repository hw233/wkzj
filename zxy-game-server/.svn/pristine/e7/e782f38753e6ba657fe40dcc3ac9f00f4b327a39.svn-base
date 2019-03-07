package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 修改装备等级请求
 * @author ludd
 *
 */
public class ModifyEquipLevelRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	/**
	 * 装备uuid
	 */
	public long uuid;
	/**
	 * 目标等级
	 */
	public int targetLevel;
	
	public ModifyEquipLevelRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.uuid = readLong();
		this.targetLevel = readInt();
	}

}
