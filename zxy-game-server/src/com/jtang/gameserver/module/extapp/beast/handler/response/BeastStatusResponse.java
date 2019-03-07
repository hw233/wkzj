package com.jtang.gameserver.module.extapp.beast.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 状态
 * @author ligang
 *
 */
public class BeastStatusResponse extends IoBufferSerializer {

	/**
	 * 状态0关1开
	 */
	public byte status;
	
	/**
	 * 血量百分比
	 */
	public byte bloodPer;
	
	public BeastStatusResponse(int status, int bloodPer){
		this.status = (byte)status;
		this.bloodPer = (byte)bloodPer;
	}
	
	@Override
	public void write() {
		writeByte(this.status);
		writeByte(this.bloodPer);
	}
	
}
