package com.jtang.gameserver.module.hole.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class HoleAllyOpenRequest extends IoBufferSerializer {
	
	/**
	 * 邀请人actorId
	 */
	public long actorId;
	/**
	 * 被邀请人actorId
	 */
	public long targetId;
	/**
	 * 洞府自增id
	 */
	public long id;
	
	
	public HoleAllyOpenRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.targetId = readLong();
		this.id = readLong();
	}

}
