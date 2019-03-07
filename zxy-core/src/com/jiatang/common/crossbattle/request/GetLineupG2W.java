package com.jiatang.common.crossbattle.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class GetLineupG2W extends IoBufferSerializer {
	/**
	 * 目标角色id
	 */
	public long targetActorId;
	public GetLineupG2W(byte[] bytes) {
		super(bytes);
	}
	public GetLineupG2W(long targetActorId) {
		super();
		this.targetActorId = targetActorId;
	}
	
	@Override
	public void write() {
		this.writeLong(this.targetActorId);
	}
	
	@Override
	protected void read() {
		this.targetActorId = readLong();
	}
	
	
}
