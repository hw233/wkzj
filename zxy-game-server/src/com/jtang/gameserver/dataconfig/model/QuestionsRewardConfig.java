package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="questionsRewardConfig")
public class QuestionsRewardConfig implements ModelAdapter {

	/**
	 * 次数
	 */
	public int count;
	
	/**
	 * 奖励
	 */
	private String reward;
	
	@FieldIgnore
	public List<RewardObject> rewardList = new ArrayList<>();

	@Override
	public void initialize() {
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for(String[] str:list){
			RewardObject rewardObject = RewardObject.valueOf(str);
			rewardList.add(rewardObject);
		}
		reward = null;
	}
}
