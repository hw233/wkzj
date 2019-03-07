package com.jtang.gameserver.module.adventures.bable.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.gameserver.dataconfig.model.BableExchangeConfig;

public class BableExchangeVO extends IoBufferSerializer {

	/**
	 * 兑换id
	 */
	public int exchangeId;
	
	/**
	 * 奖励类型
	 */
	public int type;
	
	/**
	 * 奖励id
	 */
	public int rewardId;
	
	/**
	 * 奖励数量
	 */
	public int rewardNum;
	
	/**
	 * 奖励需要通天币
	 */
	public int useStar;
	
	/**
	 * 可兑换数量
	 */
	public int exchangeNum;
	
	public BableExchangeVO(){
		
	}
	
	public BableExchangeVO(String[] str) {
		this.exchangeId = Integer.valueOf(str[0]);
		this.type = Integer.valueOf(str[1]);
		this.rewardId = Integer.valueOf(str[2]);
		this.rewardNum = Integer.valueOf(str[3]);
		this.useStar = Integer.valueOf(str[4]);
		this.exchangeNum = Integer.valueOf(str[5]);
		
	}

	public BableExchangeVO(BableExchangeConfig exchangeConfig) {
		exchangeId = exchangeConfig.exchangeId;
		type = exchangeConfig.type;
		rewardId = exchangeConfig.id;
		rewardNum = exchangeConfig.num;
		exchangeNum = RandomUtils.nextInt(exchangeConfig.minNum, exchangeConfig.maxNum);
		useStar = exchangeConfig.consumeStar;
	}

	public String parser2String(){
		StringBuffer sb = new StringBuffer();
		sb.append(exchangeId);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(type);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(rewardId);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(rewardNum);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(useStar);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(exchangeNum);
		return sb.toString();
	}
	
	@Override
	public void write() {
		writeInt(exchangeId);
		writeInt(type);
		writeInt(rewardId);
		writeInt(rewardNum);
		writeInt(useStar);
		writeInt(exchangeNum);
	}
}
