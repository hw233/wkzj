package com.jtang.gameserver.module.extapp.deitydesc.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class DeityDescendInfoResponse extends IoBufferSerializer {

	/**
	 * 当前已经点亮的字 [0,4]  99 代表已经领过仙人奖励
	 */
	public byte curIndex;
	
	/**
	 * 当前活动仙人
	 */
	public int heroId;
	
	/**
	 * 活动结束时间 (秒
	 */
	public int endTime;
	
	public DeityDescendInfoResponse(int heroId, byte charIndex, int endTime) {
		this.curIndex = charIndex;
		this.heroId = heroId;
		this.endTime = endTime;
	}
	
	@Override
	public void write() {
		writeByte(this.curIndex);
		writeInt(this.heroId);
		writeInt(this.endTime);
	}
}
