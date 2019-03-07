package com.jtang.gameserver.module.goods.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class StartComposeRequest extends IoBufferSerializer {
	
	/**
	 * 装备碎片id
	 */
	public int goodsId;
	
	public StartComposeRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.goodsId = readInt();
	}
}
