package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 潜修室等级修改
 * @author ludd
 *
 */
public class ModifyDelveLevelRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	/**
	 * 目标等级
	 */
	public int targetLevel;
	
	
	public ModifyDelveLevelRequest(byte []bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.targetLevel = readInt();

	}

}
