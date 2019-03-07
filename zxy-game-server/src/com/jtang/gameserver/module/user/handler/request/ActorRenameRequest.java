package com.jtang.gameserver.module.user.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class ActorRenameRequest extends IoBufferSerializer {
	
	/**
	 * 名称
	 */
	public String actorName;
	
	public ActorRenameRequest(byte []bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorName = readString();
	}

}
