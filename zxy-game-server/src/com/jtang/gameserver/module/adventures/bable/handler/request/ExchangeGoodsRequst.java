package com.jtang.gameserver.module.adventures.bable.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 客户端请求兑换物品
 * @author ludd
 *
 */
public class ExchangeGoodsRequst extends IoBufferSerializer {

	/**
	 * 兑换id
	 */
	public int exchangeId;
	/**
	 * 兑换数量
	 */
	public int exchangeNum;

	
	public ExchangeGoodsRequst(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.exchangeId = readInt();
		this.exchangeNum = this.readInt();
	}
}
