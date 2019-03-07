package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 添加所有物品请求协议
 * @author ludd
 *
 */
public class AddAllGoodsRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	public AddAllGoodsRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = this.readLong();
	}

}
