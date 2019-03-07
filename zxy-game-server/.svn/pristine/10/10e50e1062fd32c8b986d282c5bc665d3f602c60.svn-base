package com.jtang.gameserver.module.gift.handler.response;

import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 收取礼物响应
 * @author vinceruan
 *
 */
public class AcceptGiftResponse extends IoBufferSerializer {
	/**
	 * 物品列表,格式Map<物品全局ID, 数量>
	 */
	private Map<Long, Integer> goods;
	
	@Override
	public void write() {
		this.writeLongIntMap(goods);		
	}
	
	public AcceptGiftResponse(Map<Long, Integer> goods) {
		this.goods = goods;
	}
}
