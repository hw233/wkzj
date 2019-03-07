package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 添加金币请求协议
 * @author ludd
 *
 */
public class GiveGoldRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 添加金币数
	 */
	public int giveNum;
	public GiveGoldRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = this.readLong();
		this.giveNum = readInt();
	}

}
