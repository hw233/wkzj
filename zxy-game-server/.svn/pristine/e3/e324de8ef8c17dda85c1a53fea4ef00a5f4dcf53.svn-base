package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 修改vip等级
 * @author ludd
 *
 */
public class ModifyVipLevelRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 赠送等级
	 */
	public int level;
	public ModifyVipLevelRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.level = readInt();
	}

}
