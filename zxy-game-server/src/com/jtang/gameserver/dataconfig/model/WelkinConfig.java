package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "welkinConfig")
public class WelkinConfig implements ModelAdapter  {

	/**
	 * 配置id
	 */
	public int id;
	
	/**
	 * 到达的区间
	 */
	public String interval;
	
	/**
	 * 消耗的点券
	 */
	public int costTicket;
	
	/**
	 * 固定奖励
	 */
	public String reward;
	
	/**
	 * 第几次获得区间最好奖励
	 */
	public String rewardCount;
	
	@FieldIgnore
	public int intervalStart;
	
	@FieldIgnore
	public int intervalEnd;
	
	@FieldIgnore
	public int startCount;
	
	@FieldIgnore
	public int endCount;
	
	@FieldIgnore
	public List<RewardConfig> rewardList = new ArrayList<>();
	
	@Override
	public void initialize() {
		
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for (String[] array : list) {
			RewardConfig reward = new RewardConfig(Arrays.asList(array));
			rewardList.add(reward);
		}
		
		List<Integer> intervalList = StringUtils.delimiterString2IntList(interval, Splitable.ATTRIBUTE_SPLIT);
		intervalStart = intervalList.get(0);
		intervalEnd = intervalList.get(1);
		
		List<Integer> rewardCountList = StringUtils.delimiterString2IntList(rewardCount, Splitable.ATTRIBUTE_SPLIT);
		startCount = rewardCountList.get(0);
		endCount = rewardCountList.get(1);
		
		this.reward = null;
		this.interval = null;
		this.rewardCount = null;
	}

}
