package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.model.RandomExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.snatch.type.SnatchAchimentType;

@DataFile(fileName = "snatchAchieveConfig")
public class SnatchAchieveConfig implements ModelAdapter {
	
	/**
	 * 成就id
	 */
	public int achieveId;
	
	/**
	 * 成就类型
	 */
	public int achieveType;
	
	/**
	 * 战斗次数
	 */
	public int fightNum;
	
	/**
	 * 上一个id
	 */
	public int priorId;
	
	/**
	 * 下一个id
	 */
	public int nextId;
	
	/**
	 * 奖励
	 */
	public String reward;
	
	/**
	 * 条件达成额外产出
	 */
	public String extReward;
	
	@FieldIgnore
	private List<ExprRewardObject> rewardList = new ArrayList<>();
	
	@FieldIgnore
	public List<RandomExprRewardObject> extRewardList = new ArrayList<>();
	
	@Override
	public void initialize() {
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for(String[] str:list){
			ExprRewardObject rewardObject = ExprRewardObject.valueOf(str);
			rewardList.add(rewardObject);
		}
		reward = null;
		
		List<String[]> extList = StringUtils.delimiterString2Array(extReward);
		for(String[] str:extList){
			RandomExprRewardObject rewardObject = RandomExprRewardObject.valueOf(str);
			extRewardList.add(rewardObject);
		}
		extReward = null;
	}

	public RewardObject getExeReward(int actorLevel) {
		if(extRewardList.isEmpty()){
			return null;
		}
		Map<Integer,Integer> map = new HashMap<>();
		for (int i = 0; i < extRewardList.size(); i++) {
			map.put(i, extRewardList.get(i).rate);
		}
		Integer index = RandomUtils.randomHit(1000, map);
		if(index == null){
			return null;
		}
		RandomExprRewardObject reward = extRewardList.get(index).clone();
		reward.calculateNum(actorLevel);
		RewardObject rewardObject = new RewardObject();
		rewardObject.rewardType = reward.rewardType;
		rewardObject.id = reward.id;
		rewardObject.num = reward.num;
		return rewardObject;
	}

	public SnatchAchimentType getAchieveType() {
		return SnatchAchimentType.valueOf(achieveType);
	}
	
	public List<RewardObject> getRewardList(int level) {
		List<RewardObject> list = new ArrayList<>();
		for (ExprRewardObject rewardObject : rewardList) {
			list.add(rewardObject.clone(level));
		}
		return list;
	}

}
