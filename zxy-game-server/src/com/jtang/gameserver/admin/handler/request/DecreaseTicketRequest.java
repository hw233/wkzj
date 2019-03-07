package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 扣点券请求协议
 *
 */
public class DecreaseTicketRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 扣除点券数
	 */
	public int ticket;
	
	public DecreaseTicketRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = this.readLong();
		this.ticket = readInt();
	}

}
