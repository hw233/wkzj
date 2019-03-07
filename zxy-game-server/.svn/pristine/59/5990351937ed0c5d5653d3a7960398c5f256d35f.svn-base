package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "plantGlobalConfig")
public class PlantGlobalConfig implements ModelAdapter {

	/**
	 * 开始时间
	 */
	public String startData;
	
	/**
	 * 结束时间
	 */
	public String endData;
	
	/**
	 * 每日种植次数
	 */
	public int dayPlant;
	
	/**
	 * 额外奖励
	 */
	public String extReward;
	
	/**
	 * 保底收获次数
	 */
	public String count;
	
	/**
	 * 保底奖励
	 */
	public String reward;
	
	/**
	 * 加速消耗点券
	 */
	public String costTicket;
	
	@FieldIgnore
	public List<RewardConfig> rewardList = new ArrayList<>();
	
	@FieldIgnore
	public List<RewardConfig> extRewardList = new ArrayList<>();
	
	@FieldIgnore
	public Map<Integer,Integer> costTicketMap = new HashMap<>();
	
	@FieldIgnore
	public int startCount;
	
	@FieldIgnore
	public int endCount;
	
	@FieldIgnore
	public int start;
	
	@FieldIgnore
	public int end;
	
	@Override
	public void initialize() {
		
		Date dateStart = DateUtils.string2Date(startData, "yyyy-MM-dd HH:mm:ss");
		Long ls = dateStart.getTime() / 1000;
		start = ls.intValue();
		Date dateEnd = DateUtils.string2Date(endData, "yyyy-MM-dd HH:mm:ss");
		Long le = dateEnd.getTime() / 1000;
		end = le.intValue();
		
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for (String[] array : list) {
			RewardConfig reward = new RewardConfig(Arrays.asList(array));
			rewardList.add(reward);
		}
		
		List<String[]> extList = StringUtils.delimiterString2Array(extReward);
		for (String[] array : extList) {
			RewardConfig reward = new RewardConfig(Arrays.asList(array));
			extRewardList.add(reward);
		}
		
		List<Integer> rewardList = StringUtils.delimiterString2IntList(count, Splitable.ATTRIBUTE_SPLIT);
		startCount = rewardList.get(0);
		endCount = rewardList.get(1);
		
		costTicketMap = StringUtils.delimiterString2IntMap(costTicket);
		
		this.startData = null;
		this.endData = null;
		this.reward = null;
		this.extReward = null;
		this.count = null;
		this.costTicket = null;
	}

}
