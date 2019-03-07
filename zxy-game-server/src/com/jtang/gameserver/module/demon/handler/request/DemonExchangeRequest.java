package com.jtang.gameserver.module.demon.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 降魔积分兑换请求
 * @author ludd
 *
 */
public class DemonExchangeRequest extends IoBufferSerializer {

	/**
	 * 奖励id
	 */
	public int id;
	
	/**
	 * 兑换数量
	 */
	public int num;
	
	public DemonExchangeRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.id = readInt();
		this.num = readInt();
	}

}
