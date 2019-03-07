package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "ladderRankRewardConfig")
public class LadderRankRewardConfig implements ModelAdapter {

	/**
	 * 排名区间
	 */
	public String rank;
	
	/**
	 * 奖励
	 */
	public String reward;
	
	/**
	 * 解锁的头像
	 */
	public String unLockIcon;
	
	@FieldIgnore
	public List<RewardObject> rewardList = new ArrayList<>();
	
	@FieldIgnore
	public int startRank;
	
	@FieldIgnore
	public int endRank;
	
	@FieldIgnore
	public List<Integer> unLockList = new ArrayList<>();
	
	@Override
	public void initialize() {
		
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for(String[] str:list){
			RewardObject rewardObject = RewardObject.valueOf(str);
			rewardList.add(rewardObject);
		}
		reward = null;
		
		List<Integer> rankList = StringUtils.delimiterString2IntList(rank, Splitable.ATTRIBUTE_SPLIT);
		startRank = rankList.get(0);
		endRank = rankList.get(1);
		
		List<Integer> iconList = StringUtils.delimiterString2IntList(unLockIcon, Splitable.ATTRIBUTE_SPLIT);
		for(Integer icon:iconList){
			unLockList.add(icon);
		}
		unLockIcon = null;
	}

}
