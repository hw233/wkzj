package com.jiatang.common.crossbattle.response;

import com.jiatang.common.crossbattle.model.ViewLineupVO;
import com.jtang.core.protocol.IoBufferSerializer;

public class LineupW2G extends IoBufferSerializer {

	public ViewLineupVO viewLineupVO;

	public LineupW2G(byte[] bytes) {
		super(bytes);
	}

	public LineupW2G(ViewLineupVO viewLineupVO) {
		super();
		this.viewLineupVO = viewLineupVO;
	}
	
	@Override
	protected void read() {
		this.viewLineupVO = new ViewLineupVO();
		this.viewLineupVO.readBuffer(this);
		
	}
	
	@Override
	public void write() {
		this.writeBytes(this.viewLineupVO.getBytes());
	}
	
}
