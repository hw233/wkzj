package com.jtang.gameserver.module.snatch.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 兑换请求
 * @author liujian
 *
 */
public class ExchangeRequest extends IoBufferSerializer {
	/**
	 * 类型, {@code ExchangeType}
	 * 1. 成就兑换奖励
	 * 2. 积分兑换
	 */
	public int type;
	
	/**
	 * 兑换配置id
	 */
	public int exchangeId;
	
	/**
	 * 兑换数量
	 * @param bytes
	 */
	public int num;
	
	public ExchangeRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		type = this.readByte();
		exchangeId = this.readInt();
		num = this.readInt();
	}


}
