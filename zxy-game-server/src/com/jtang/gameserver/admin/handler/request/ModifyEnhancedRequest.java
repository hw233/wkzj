package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class ModifyEnhancedRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	/**
	 * 目标等级
	 */
	public int targetLevel;
	
	
	
	public ModifyEnhancedRequest(byte bytes[]){
		super(bytes);
	}
	
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.targetLevel = readInt();
	}

}
