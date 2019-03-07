package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RandomRewardObject;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="storyAppConfig")
public class StoryAppConfig implements ModelAdapter {

	/**
	 * 活动开始时间
	 */
	public String startTime;
	
	/**
	 * 活动结束时间
	 */
	public String endTime;
	
	/**
	 * 奖励
	 */
	public String reward;
	
	/**
	 * 几率类型
	 * 1.奖励几率总和为1000时多个随机一个奖励
	 * 2.奖励几率总和小于1000时有可能没有奖励
	 * 3.奖励几率都为1000时,发放所有奖励
	 */
	public int propertionType;
	
	@FieldIgnore
	public int start;
	
	@FieldIgnore
	public int end;
	
	@FieldIgnore
	public List<RandomRewardObject> rewardList = new ArrayList<>();
	
	@Override
	public void initialize() {
		Date dateStart = DateUtils.string2Date(startTime, "yyyy-MM-dd HH:mm:ss");
		Long ls = dateStart.getTime() / 1000;
		start = ls.intValue();
		startTime = null;
		
		Date dateEnd = DateUtils.string2Date(endTime, "yyyy-MM-dd HH:mm:ss");
		Long le = dateEnd.getTime() / 1000;
		end = le.intValue();
		endTime = null;
		
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for (String[] str : list) {
			RandomRewardObject randomRewardObject = RandomRewardObject.valueOf(str);
			rewardList.add(randomRewardObject);
		}
		reward = null;
	}

}
