package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 删除物品请求
 * @author ludd
 *
 */
public class DeleteGoodsRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	/**
	 * 物品id
	 */
	public int goodsId;
	
	/**
	 * 删除数量
	 */
	public int num;
	
	public DeleteGoodsRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.goodsId = readInt();
		this.num = readInt();
	}

}
