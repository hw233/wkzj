package com.jtang.gameserver.module.goods.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class SellGoodsResponse extends IoBufferSerializer {

	/**
	 * 获得的金币
	 */
	public int golds;
	
	public SellGoodsResponse(int golds){
		this.golds = golds;
	}
	
	@Override
	public void write() {
		writeInt(golds);
	}
	
}
