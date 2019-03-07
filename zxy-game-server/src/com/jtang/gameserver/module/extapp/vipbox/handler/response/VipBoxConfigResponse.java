package com.jtang.gameserver.module.extapp.vipbox.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class VipBoxConfigResponse extends IoBufferSerializer {

	/**
	 * 活动开启时间
	 */
	public int startTime;
	
	/**
	 * 活动结束时间
	 */
	public int endTime;
	
	public VipBoxConfigResponse(int startTime,int endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	@Override
	public void write() {
		writeInt(startTime);
		writeInt(endTime);
	}
}
