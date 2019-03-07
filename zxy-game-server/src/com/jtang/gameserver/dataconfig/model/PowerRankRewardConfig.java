package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "powerRankRewardConfig")
public class PowerRankRewardConfig implements ModelAdapter {
	
	/**
	 * 名次
	 */
	public int rank;
	
	/**
	 * 奖励列表
	 */
	private String reward;
	
	@FieldIgnore
	private List<RewardObject> rewardList = new ArrayList<>();

	@Override
	public void initialize() {
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for(String[] str:list){
			RewardObject rewardObject = RewardObject.valueOf(str);
			rewardList.add(rewardObject);
		}
	}
	
	public List<RewardObject> getReward(){
		return rewardList;
	}

}
