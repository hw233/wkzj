package com.jiatang.common.crossbattle.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class ActorPointW2G extends IoBufferSerializer {
	/**
	 * 玩家积分
	 */
	public int point;

	public ActorPointW2G(byte[] bytes) {
		super(bytes);
	}
	
	public ActorPointW2G(int point){
		this.point = point;
	}

	@Override
	protected void read() {
		this.point = readInt();
	}

	@Override
	public void write() {
		writeInt(point);
	}
}
