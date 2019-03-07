package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "ladderFightConfig")
public class LadderFightConfig implements ModelAdapter {

	/**
	 * 挑战类型 1.向上挑战(大于等于自己等级) 2.向下挑战(小于自己等级)
	 */
	public int fightType;

	/**
	 * 奖励
	 */
	public String reward;
	
	/**
	 * 几率(同挑战类型几率合为1000)
	 */
	public int rate;

	@FieldIgnore
	public List<RewardObject> rewardList = new ArrayList<>();

	@Override
	public void initialize() {
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for (String[] str : list) {
			RewardObject rewardObject = RewardObject.valueOf(str);
			rewardList.add(rewardObject);
		}
		reward = null;
	}

}
