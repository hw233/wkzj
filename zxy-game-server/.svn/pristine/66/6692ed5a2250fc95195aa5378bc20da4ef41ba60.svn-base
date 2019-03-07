package com.jtang.gameserver.module.goods.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class SellGoodsRequest extends IoBufferSerializer {

	/**
	 * 物品id
	 */
	public int goodsId;
	
	/**
	 * 物品数量
	 */
	public int goodsNum;
	
	public SellGoodsRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.goodsId = readInt();
		this.goodsNum = readInt();
	}
}
