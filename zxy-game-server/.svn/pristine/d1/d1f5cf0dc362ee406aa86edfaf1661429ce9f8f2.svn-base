package com.jtang.gameserver.module.vampiir.handler.request;

import java.util.List;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 吸灵操作请求 消息结构为:吸灵英雄ID(int)/被吸灵的英雄ID序列
 * 
 * @author pengzy
 * 
 */
public class DoVampiirRequest extends IoBufferSerializer {
	/**
	 * 选择的英雄id
	 */
	public int heroId;

	/**
	 * 被(消耗)吸灵的仙人和魂魄id列表
	 */
	public List<Integer> heroIds;
	/**
	 * 仙人灵魂
	 */
	public Map<Integer, Integer> heroSouls;
	/**
	 * 物品列表
	 * key：物品id
	 * value：物品数量
	 */
	public Map<Integer, Integer> goods;
	
	public DoVampiirRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		heroId = readInt();// 进行吸灵的英雄ID
		heroIds = readIntegerList();
		
		heroSouls = readIntMap();
		goods = readIntMap();
		
//		short soulSize = this.readShort();
//		for (int i = 0; i < soulSize; i++) {
//			int heroSoulId = readInt();//仙人魂魄ID
//			int sum = readInt();//仙人魂魄数量
//			heroSouls.put(heroSoulId, sum);
//		}
//		
//		short goodsSize = this.readShort();
//		for (int i = 0; i < goodsSize; i++) {
//			int goodsId = this.readInt();
//			int goodsNum = this.readInt();
//			goods.put(goodsId, goodsNum);
//		}
	}
}
