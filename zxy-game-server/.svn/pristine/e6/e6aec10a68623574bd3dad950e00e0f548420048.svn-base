package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RandomRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="loveMonsterRewardConfig")
public class LoveMonsterRewardConfig implements ModelAdapter {

	/**
	 * 难度
	 */
	public int difficultId;
	
	/**
	 * 活动当天开始时间(yyyy-mm-dd hh:mm:ss)
	 */
	private String time;
	
	/**
	 * 奖励(类型(详见rewrdType)_id_数量|...)
	 */
	private String reward;
	
	private String least;
	
	private String leastReward;
	
	@FieldIgnore
	private int begin;
	
	@FieldIgnore
	private int end;
	
	@FieldIgnore
	private Date start;
	
	@FieldIgnore
	private List<RandomRewardObject> rewardList = new ArrayList<>();
	@FieldIgnore
	private List<RewardObject> leastrewardList = new ArrayList<>();
	
	@Override
	public void initialize() {
		start = DateUtils.string2Date(time, "yyyy-MM-dd HH:mm:ss");
		
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for (String[] str : list) {
			RandomRewardObject reward = RandomRewardObject.valueOf(str);
			rewardList.add(reward);
		}
		
		List<Integer> leastList = StringUtils.delimiterString2IntList(least, Splitable.ATTRIBUTE_SPLIT);
		this.begin = leastList.get(0);
		this.end = leastList.get(1);
		this.least = null;
		
		List<String[]> list1 = StringUtils.delimiterString2Array(leastReward);
		for(String[] str:list1){
			RewardObject rewardObject = RewardObject.valueOf(str);
			leastrewardList.add(rewardObject);
		}
		this.reward = null;
		this.leastReward = null;
	}
	
	
	public int randomLeast(){
		return RandomUtils.nextInt(begin, end);
	}
	
	public boolean isReward(){
		return DateUtils.getRemainDays(start, new Date()) == 0 ? true : false;
	}
	
	public List<RewardObject> getReward(){
		List<RewardObject> list = new ArrayList<>();
		if(rewardList.isEmpty() == false){
			int rate = 0;
			int propertity = RandomUtils.nextInt(0, 1000);
			for (RandomRewardObject reward : rewardList) {
				if(propertity < rate){
					list.add(reward);
				}
				rate += reward.rate;
			}
		}
		return list;
	}
	
	public List<RewardObject> getLeastrewardList() {
		return leastrewardList;
	}

}
