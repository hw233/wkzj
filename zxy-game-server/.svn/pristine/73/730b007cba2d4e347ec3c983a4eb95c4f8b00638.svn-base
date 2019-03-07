package com.jtang.gameserver.module.goods.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 使用物品请求
 * @author 0x737263
 *
 */
public class UseGoodsRequest extends IoBufferSerializer {

	/**
	 * 物品uuid
	 */
	public long uuid;
	
	/**
	 * 物品配置id
	 */
	public int goodsId;
	
	/**
	 * 使用数量
	 */
	public int useNum;
	
	/**
	 * 使用标记（目前为了使用精活丹需要确定是选择精力还是活力（精力1，活力2）没有标记填0）
	 */
	public int useFlag;

	
	public String phoneNum;
	
	public UseGoodsRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.uuid = readLong();
		this.goodsId = readInt();
		this.useNum = readInt();
		this.useFlag = readByte();
		this.phoneNum = readString();
	}

}
