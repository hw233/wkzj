package com.jiatang.common.crossbattle.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class AllEndW2G extends IoBufferSerializer {
	
	public int serverScoreRank;
	/**
	 * 赛季结束奖励
	 */
	public String rewardObjects;

	public AllEndW2G(int serverScoreRank, String rewardObjects) {
		super();
		this.serverScoreRank = serverScoreRank;
		this.rewardObjects = rewardObjects;
	}

	public AllEndW2G(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	protected void read() {
		this.serverScoreRank = readInt();
		this.rewardObjects = readString();
	}
	
	@Override
	public void write() {
		this.writeInt(this.serverScoreRank);
		this.writeString(this.rewardObjects);
	}
	
	
	
	
	
	
}
