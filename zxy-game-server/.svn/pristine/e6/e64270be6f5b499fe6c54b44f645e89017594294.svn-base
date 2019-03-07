package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 赠送点请求协议
 * @author ludd
 *
 */
public class GiveTicketRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 赠送点券数
	 */
	public int giveNum;
	public GiveTicketRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = this.readLong();
		this.giveNum = readInt();
	}

}
