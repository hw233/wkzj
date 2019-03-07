package com.jtang.gameserver.module.love.handler.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.love.model.LoveShopVO;

public class LoveShopInfoResponse extends IoBufferSerializer {

	/**
	 * 物品数量
	 */
	public int goodsNum;
	
	/**
	 * 商品列表
	 */
	public Map<Integer,LoveShopVO> shopMap = new HashMap<>();
	
	/**
	 * 刷新次数
	 */
	public int flushNum;
	
	public LoveShopInfoResponse(int goodsNum,Map<Integer,LoveShopVO> shopList, int flushNum) {
		this.goodsNum = goodsNum;
		this.shopMap = shopList;
		this.flushNum = flushNum;
	}

	@Override
	public void write() {
		super.write();
		writeInt(goodsNum);
		writeShort((short)shopMap.size());
		for(Entry<Integer,LoveShopVO> entry : shopMap.entrySet()){
			writeInt(entry.getKey());
			writeBytes(entry.getValue().getBytes());
		}
		writeInt(this.flushNum);
	}
}
