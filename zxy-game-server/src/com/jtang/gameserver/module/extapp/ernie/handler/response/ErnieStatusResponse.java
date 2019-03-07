package com.jtang.gameserver.module.extapp.ernie.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 摇奖信息
 * @author lig
 *
 */
public class ErnieStatusResponse extends IoBufferSerializer {
	
	/*
	 * 开关状态(1 开 ,2 关)
	 */
	public byte status;

	public ErnieStatusResponse(int status) {
		this.status = (byte)status;
	}
	
	@Override
	public void write() {
		this.writeByte(this.status);
	}
}
