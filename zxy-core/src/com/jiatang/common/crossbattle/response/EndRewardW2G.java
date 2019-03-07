package com.jiatang.common.crossbattle.response;

import com.jiatang.common.crossbattle.model.DayEndRewardVO;
import com.jtang.core.protocol.IoBufferSerializer;

public class EndRewardW2G extends IoBufferSerializer {
	public DayEndRewardVO endReward;

	public EndRewardW2G(DayEndRewardVO endReward) {
		super();
		this.endReward = endReward;
	}

	public EndRewardW2G(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	protected void read() {
		this.endReward = new DayEndRewardVO();
		this.endReward.readBuffer(this);
	}
	
	@Override
	public void write() {
		this.writeBytes(this.endReward.getBytes());
	}
	
	
}
