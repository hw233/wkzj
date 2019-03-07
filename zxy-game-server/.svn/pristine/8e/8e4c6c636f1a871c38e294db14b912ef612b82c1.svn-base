package com.jtang.gameserver.module.treasure.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class TreasureGoodsResponse extends IoBufferSerializer {
	
	/**
	 * 兑换物品的数量
	 * @param item
	 */
	public int num;

	public TreasureGoodsResponse(int num) {
		this.num = num;
	}
	
	@Override
	public void write() {
		writeInt(num);
	}

}
