package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 扣金币请求协议
 * @author ludd
 *
 */
public class DecreaseGoldRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 添加金币数
	 */
	public int decreaseNum;
	public DecreaseGoldRequest(byte[] bytes) {
		super(bytes);
	}
	@Override
	public void read() {
		this.actorId = this.readLong();
		this.decreaseNum = readInt();
	}

}
