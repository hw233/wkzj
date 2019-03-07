package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 修改吸灵室等级
 * @author ludd
 *
 */
public class ModifyVampiirRequest extends IoBufferSerializer {
	/**
	 *  角色id
	 */
	public long actorId;
	/**
	 * 目标等级
	 */
	public int targetLevel;
	
	public ModifyVampiirRequest(byte[] bytes) {
		super(bytes);
	}
	
	public void read() {
		this.actorId = readLong();
		this.targetLevel = readInt();
	}

}
