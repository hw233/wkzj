package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName="vipBoxRewardConfig")
public class VipBoxRewardConfig implements ModelAdapter {

	/**
	 * 开启次数
	 */
	public int openCount;
	
	/**
	 * 奖励序号
	 */
	public int index;
	
	/**
	 * 奖励类型
	 */
	public int rewardType;
	
	/**
	 * 奖励id
	 */
	public int rewardId;
	
	/**
	 * 奖励数量
	 */
	public int rewardNum;
	
	/**
	 * 奖励几率
	 */
	public int rewardRate;
	
	/**
	 * 是否必出
	 * 0.否 1.是
	 */
	public int mustReward;
	
	@Override
	public void initialize() {
		
	}

	public boolean isMustReward() {
		return mustReward == 1;
	}

}
