package com.jtang.gameserver.module.story.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 拾取物品请求
 * @author vinceruan
 *
 */
public class PickupGoodsRequest extends IoBufferSerializer {
	public String fightId;
	public String goodsUuid;
	
	@Override
	public void read() {
		fightId = readString();
		goodsUuid = readString();
	}	
	
	public PickupGoodsRequest(byte bytes[]) {
		super(bytes);
	}
}
