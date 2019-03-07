package com.jtang.gameserver.module.demon.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class DemonLastRewardResponse extends IoBufferSerializer {

	/**
	 * 是否已读（1：表示已读，0：表示未读)
	 */
	private byte isRead;
	
	/**
	 * 奖励数据
	 */
	private DemonEndRewardResponse demonEndRewardResponse;
	
	
	public DemonLastRewardResponse() {
		isRead = (byte) 1;
		demonEndRewardResponse = new DemonEndRewardResponse();
	}
	
	public DemonLastRewardResponse(byte isRead, DemonEndRewardResponse demonEndRewardResponse) {
		super();
		this.isRead = isRead;
		this.demonEndRewardResponse = demonEndRewardResponse;
	}


	@Override
	public void write() {

		this.writeByte(this.isRead);
		this.writeBytes(demonEndRewardResponse.getBytes());
	}

}
