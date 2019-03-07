package com.jiatang.common.crossbattle.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class ExchangePointG2W extends IoBufferSerializer {
	
	/**
	 * 兑换配置id
	 */
	public int configId;
	
	public ExchangePointG2W(byte[] bytes) {
		super(bytes);
	}
	public ExchangePointG2W(int configId, long actorId) {
		super();
		this.configId = configId;
	}
	
	@Override
	public void write() {
		this.writeInt(this.configId);
	}
	
	@Override
	protected void read() {
		this.configId = readInt();
	}
	
}
