package com.jtang.gameserver.module.adventures.shop.shop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

public class ShopVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8811717784100609550L;

	/**
	 * 商品id
	 * */
	public int shopId;

	/**
	 * 购买次数
	 * */
	public int buyCount;

	/**
	 * 重置时间 0为永久 1为每天
	 * */
	public int resetTime;

	public ShopVO(int shopId, int buyCount) {
		this.shopId = shopId;
		this.buyCount = buyCount;
		this.resetTime = 1;
	}

	public ShopVO(int shopId, int buyCount, int resetTime) {
		this.shopId = shopId;
		this.buyCount = buyCount;
		this.resetTime = resetTime;
	}

	public String parse2String() {
		List<Object> list = new ArrayList<Object>();
		list.add(shopId);
		list.add(buyCount);
		list.add(resetTime);
		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}

	public static ShopVO valueOf(int shopId) {
		ShopVO shopVO = new ShopVO(shopId, 0);
		return shopVO;
	}

	public static ShopVO valueOf(String[] array) {
		ShopVO shopVO=null;
		if(array.length == 3){
			shopVO = new ShopVO(Integer.valueOf(array[0]), Integer.valueOf(array[1]), Integer.valueOf(array[2]));
		}else{
			shopVO = new ShopVO(Integer.valueOf(array[0]), Integer.valueOf(array[1]));
		}
		return shopVO;
	}

	public void writePacket(IoBufferSerializer packet) {
		packet.writeInt(shopId);
		packet.writeInt(buyCount);
	}
	
}
