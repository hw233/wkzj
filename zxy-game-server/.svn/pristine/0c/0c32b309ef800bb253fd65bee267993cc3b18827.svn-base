package com.jtang.gameserver.module.extapp.plant.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class PlantStateResponse extends IoBufferSerializer {
	
	/**
	 * 种植活动状态
	 * 0.关闭 1.开启
	 */
	public int state;
	
	
	public PlantStateResponse(int state){
		this.state = state;
	}
	
	@Override
	public void write() {
		writeInt(state);
	}
}
