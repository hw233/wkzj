package com.jtang.core.model;

import com.jtang.core.utility.StringUtils;
/**
 * 带概率的奖励物品
 * @author ludd
 *
 */
public class RandomRewardObject extends RewardObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5492166986254680892L;
	/**
	 * 概率千分比
	 */
	public int rate;
	
	public static RandomRewardObject valueOf(String[] record) {
		record = StringUtils.fillStringArray(record, 4, "0");
		RandomRewardObject rewardObject = new RandomRewardObject();
		rewardObject.rewardType = RewardType.getType(Integer.valueOf(record[0]));
		rewardObject.id = Integer.valueOf(record[1]);
		rewardObject.num = Integer.valueOf(record[2]);
		rewardObject.rate = Integer.valueOf(record[3]);
		return rewardObject;
	}
	
}
