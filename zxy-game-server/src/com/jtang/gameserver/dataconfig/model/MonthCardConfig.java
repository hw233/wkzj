package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "monthCardConfig")
public class MonthCardConfig implements ModelAdapter {

	/**
	 * id
	 */
	public int rechargeType;
	
	/**
	 * 首冲月卡奖励
	 */
	public String firstReward;
	
	/**
	 * 充值赠送可领天数
	 */
	public int day;
	
	/**
	 * 剩余多少天可以充值
	 */
	public int endDay;
	
	/**
	 * 每天领取奖励
	 */
	public String dayReward;
	
	@FieldIgnore
	public List<RewardObject> firstList = new ArrayList<>();
	
	@FieldIgnore
	public List<ExprRewardObject> dayList = new ArrayList<>();
	
	@Override
	public void initialize() {
		List<String[]> rewardList = StringUtils.delimiterString2Array(firstReward);
		for(String[] str:rewardList){
			RewardObject rewardObject = RewardObject.valueOf(str);
			firstList.add(rewardObject);
		}
		
		List<String[]> reward = StringUtils.delimiterString2Array(dayReward);
		for(String[] str:reward){
			ExprRewardObject rewardObject = ExprRewardObject.valueOf(str);
			dayList.add(rewardObject);
		}
		
	}

}
