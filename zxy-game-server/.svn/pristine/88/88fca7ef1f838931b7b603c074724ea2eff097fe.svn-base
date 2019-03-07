package com.jtang.gameserver.module.vampiir.model;

import com.jtang.core.protocol.IoBufferSerializer;

public class ExchangeVO extends IoBufferSerializer {
	
	/**
	 * 物品id
	 */
	public int goodsId;
	
	/**
	 * 物品数量
	 */
	public int num;
	
	public ExchangeVO(int goodsId,int num){
		this.goodsId = goodsId;
		this.num = num;
	}
	
	@Override
	public void write() {
		writeInt(goodsId);
		writeInt(num);
	}

}
