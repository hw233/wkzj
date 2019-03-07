package com.jiatang.common.crossbattle.response;

import com.jiatang.common.crossbattle.model.ActorAttributeChangeVO;
import com.jtang.core.protocol.IoBufferSerializer;

public class ActorAttributeChangeW2G extends IoBufferSerializer {
	
	public ActorAttributeChangeVO actorAttributeChangeVO;
	public ActorAttributeChangeW2G() {
		super();
	}
	


	public ActorAttributeChangeW2G(ActorAttributeChangeVO actorAttributeChangeVO) {
		super();
		this.actorAttributeChangeVO = actorAttributeChangeVO;
	}



	public ActorAttributeChangeW2G(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	protected void read() {
		this.actorAttributeChangeVO = new ActorAttributeChangeVO();
		this.actorAttributeChangeVO.readBuffer(this);
	}
	
	@Override
	public void write() {
		this.writeBytes(this.actorAttributeChangeVO.getBytes());
	}
	
	
}
