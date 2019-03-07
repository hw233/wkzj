package com.jiatang.common.crossbattle.response;

import com.jiatang.common.crossbattle.model.EndNoticeVO;
import com.jtang.core.protocol.IoBufferSerializer;

public class EndNoticeW2G extends IoBufferSerializer {
	public EndNoticeVO endNoticeVO;

	public EndNoticeW2G(EndNoticeVO endNoticeVO) {
		super();
		this.endNoticeVO = endNoticeVO;
	}
	
	
	
	public EndNoticeW2G(byte[] bytes) {
		super(bytes);
	}



	@Override
	public void write() {
		this.writeBytes(this.endNoticeVO.getBytes());
	}
	
	@Override
	protected void read() {
		this.endNoticeVO = new EndNoticeVO();
		this.endNoticeVO.readBuffer(this);
	}
	
}
