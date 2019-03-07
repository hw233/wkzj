package com.jiatang.common.crossbattle.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class AttackActorG2W extends IoBufferSerializer {

	public long targetActorId;
	
	
	public AttackActorG2W(long actorId, long targetActorId) {
		super();
		this.targetActorId = targetActorId;
	}

	

	public AttackActorG2W(byte[] bytes) {
		super(bytes);
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
