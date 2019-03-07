package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 添加物品请求协议
 * @author ludd
 *
 */
public class GiveGoodsRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 物品id
	 */
	public int goodsId;
	
	/**
	 * 数量
	 */
	public int num;
	public GiveGoodsRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = this.readLong();
		this.goodsId = readInt();
		this.num = readInt();
	}

}
