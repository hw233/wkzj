package com.jtang.gameserver.module.extapp.beast.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 血量
 * @author ligang
 *
 */
public class BeastBloodResponse extends IoBufferSerializer {

	
	/**
	 * 血量百分比
	 */
	public byte bloodPer;
	
	public BeastBloodResponse(int status){
		this.bloodPer = (byte)bloodPer;
	}
	
	@Override
	public void write() {
		writeByte(this.bloodPer);
	}
	
}
