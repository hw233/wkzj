package com.jtang.gameserver.module.adventures.shop.trader.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

public class ItemVO extends IoBufferSerializer {

	/**
	 * 商品id
	 */
	public int id;
	
	/**
	 * 物品id
	 */
	public int itemId;
	
	/**
	 * 物品类型
	 */
	public int itemType;
	
	/**
	 * 物品数量
	 */
	public int itemNum;
	
	/**
	 * 物品折扣
	 */
	public int discount;
	
	/**
	 * 购买消耗的金币
	 */
	public int costGold;
	
	/**
	 * 购买消耗的点券
	 */
	public int costTicket;
	
	public ItemVO() {
		
	}
	
	
	public String parser2String(){
		StringBuffer sb = new StringBuffer();
		sb.append(id).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(itemId).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(itemNum).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(itemType).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(discount).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(costGold).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(costTicket);
		return sb.toString();
	}
	
	public static ItemVO valueOf(String str[]){
		str = StringUtils.fillStringArray(str, 7, "0");
		ItemVO itemVO = new ItemVO();
		itemVO.id = Integer.valueOf(str[0]);
		itemVO.itemId = Integer.valueOf(str[1]);
		itemVO.itemNum = Integer.valueOf(str[2]);
		itemVO.itemType = Integer.valueOf(str[3]);
		itemVO.discount = Integer.valueOf(str[4]);
		itemVO.costGold = Integer.valueOf(str[5]);
		itemVO.costTicket = Integer.valueOf(str[6]);
		return itemVO;
	}
	
	@Override
	public void write() {
		writeInt(id);
		writeInt(itemId);
		writeInt(itemNum);
		writeInt(itemType);
		writeInt(discount);
		writeInt(costGold);
		writeInt(costTicket);
	}
}
