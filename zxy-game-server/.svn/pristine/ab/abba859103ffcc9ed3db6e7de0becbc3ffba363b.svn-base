package com.jtang.gameserver.module.snatch.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.gameserver.dataconfig.model.SnatchExchangeConfig;

public class ExchangeVO extends IoBufferSerializer {
	
	/**
	 * 兑换id
	 */
	public int exchangeId;
	
	/**
	 * 消耗积分
	 */
	public int costScore;
	
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
		costScore = Integer.valueOf(str[1]);
		type = Byte.valueOf(str[2]);
		id = Integer.valueOf(str[3]);
		num = Integer.valueOf(str[4]);
		isExchange = Byte.valueOf(str[5]);
	}
	
	@Override
	public void write() {
		writeInt(exchangeId);
		writeInt(costScore);
		writeByte(type);
		writeInt(id);
		writeInt(num);
		writeByte(isExchange);
	}

	public String parser2String() {
		StringBuffer sb = new StringBuffer();
		sb.append(exchangeId).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(costScore).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(type).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(id).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(num).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(isExchange);
		return sb.toString();
	}
	
	public static ExchangeVO valueOf(SnatchExchangeConfig config){
		ExchangeVO exchangeVO = new ExchangeVO();
		exchangeVO.exchangeId = config.exchangeId;
		exchangeVO.costScore = config.needScore;
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
