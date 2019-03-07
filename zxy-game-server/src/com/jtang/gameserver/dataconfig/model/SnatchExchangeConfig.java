package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName="snatchExchangeConfig")
public class SnatchExchangeConfig implements ModelAdapter {
	/**
	 * 兑换id
	 */
	public int exchangeId;
	
	/**
	 * 需要的兑换券
	 */
	public int needScore;
	
	/**
	 * 奖励id
	 */
	public int rewardId;
	
	/**
	 * 奖励类型
	 */
	public int rewardType;
	
	/**
	 * 奖励数量
	 */
	public int rewardNum;
	
//	/**
//	 * 几率
//	 */
//	public int proportion;
//	
//	/**
//	 * 是否必出
//	 * 0.否 1.是
//	 */
//	public int mastReward;
	
	@Override
	public void initialize() {
	}

//	public boolean isMastReward() {
//		return mastReward == 1 ? true : false;
//	}

}
