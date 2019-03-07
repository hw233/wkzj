package com.jtang.gameserver.module.adventures.shop.trader.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.gameserver.dbproxy.entity.Trader;
import com.jtang.gameserver.module.adventures.shop.trader.model.ItemVO;
import com.jtang.gameserver.module.adventures.shop.trader.type.ShopType;


public class TraderShopInfoResponse extends ShopInfoResponse {
	
	/**
	 * 免费刷新剩余次数
	 */
	public int freeNum;
	
	/**
	 * 刷新需要的点券
	 */
	public int costTicket;
	
	/**
	 * 刷新符数量
	 */
	public int flushGoods;
	
	/**
	 * 商品列表
	 */
	public List<ItemVO> goodsList;

	public TraderShopInfoResponse(ShopType shopType,Trader trader,int costTicket,int flushGoods) {
		super(shopType);
		this.freeNum = trader.freeNum;
		this.costTicket = costTicket;
		this.flushGoods = flushGoods;
		this.goodsList = new ArrayList<>(trader.itemMap.values());
	}
	
	@Override
	public void write() {
		super.write();
		writeInt(freeNum);
		writeInt(costTicket);
		writeInt(flushGoods);
		writeShort((short) goodsList.size());
		for(ItemVO itemVO:goodsList){
			writeBytes(itemVO.getBytes());
		}
	}

}
