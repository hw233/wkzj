package com.jtang.gameserver.module.vampiir.handler.request;

import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 吸灵操作请求 消息结构为:吸灵英雄ID(int)/被吸灵的英雄ID序列
 * 
 * @author pengzy
 * 
 */
public class ExchangeHeroSoulRequest extends IoBufferSerializer {

	/**
	 * 仙人灵魂
	 */
	public Map<Integer, Integer> heroSouls;
	
	public ExchangeHeroSoulRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		heroSouls = readIntMap();
//		
//		short goodsSize = this.readShort();
//		for (int i = 0; i < goodsSize; i++) {
//			int goodsId = this.readInt();
//			int goodsNum = this.readInt();
//			goods.put(goodsId, goodsNum);
//		}
	}
}
