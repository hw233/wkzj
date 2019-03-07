package com.jiatang.common.crossbattle.response;

import com.jiatang.common.crossbattle.model.CrossData;
import com.jtang.core.protocol.IoBufferSerializer;

public class ActorCrossDataW2G extends IoBufferSerializer {
	public CrossData crossData;
	public ActorCrossDataW2G(byte[] bytes) {
		super(bytes);
	}
	
	
	
	
	public ActorCrossDataW2G(CrossData crossData) {
		super();
		this.crossData = crossData;
	}




	@Override
	public void read() {
		crossData = new CrossData();
		crossData.readBuffer(this);
	}
	@Override
	public void write() {
		this.writeBytes(crossData.getBytes());
	}

}
