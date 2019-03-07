package com.jtang.gameserver.module.adventures.shop.shop.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.gameserver.dataconfig.model.BlackShopRewardConfig;

public class ExchangeVO extends IoBufferSerializer {
	
	/**
	 * 商品id
	 */
	public int exchangeId;
	
	/**
	 * 消耗点券
	 */
	public int costTicket;
	
	/**
	 * 消耗金币
	 */
	public int costGold;
	
	/**
	 * 类型
	 */
	public byte type;
	
	/**
	 * id
	 */
	public int id;
	
	/**
	 * 数量
	 */
	public int num;
	
	
	/**
	 * 物品是否已兑换
	 * 0.否 1.是
	 */
	private byte isExchange;
	
	
	public ExchangeVO(){
	}
	
	public ExchangeVO(String[] str){
		exchangeId = Integer.valueOf(str[0]);
		costTicket = Integer.valueOf(str[1]);
		costGold = Integer.valueOf(str[2]);
		type = Byte.valueOf(str[3]);
		id = Integer.valueOf(str[4]);
		num = Integer.valueOf(str[5]);
		isExchange = Byte.valueOf(str[6]);
	}
	
	@Override
	public void write() {
		writeInt(exchangeId);
		writeInt(costTicket);
		writeInt(costGold);
		writeByte(type);
		writeInt(id);
		writeInt(num);
		writeByte(isExchange);
	}

	public String parser2String() {
		StringBuffer sb = new StringBuffer();
		sb.append(exchangeId).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(costTicket).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(costGold).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(type).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(id).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(num).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(isExchange);
		return sb.toString();
	}
	
	public static ExchangeVO valueOf(BlackShopRewardConfig config){
		ExchangeVO exchangeVO = new ExchangeVO();
		exchangeVO.exchangeId = config.exchangeId;
		exchangeVO.costTicket = config.needTicket;
		exchangeVO.costGold = config.needGold;
		exchangeVO.type = (byte) config.rewardType;
		exchangeVO.id = config.rewardId;
		exchangeVO.num = config.rewardNum;
		exchangeVO.isExchange = 0;
		return exchangeVO;
	}
	
	public boolean isExchange() {
		return isExchange == 1 ? true : false;
	}

	public void setExchange() {
		this.isExchange = 1;
	}
}
