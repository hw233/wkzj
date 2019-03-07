package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "plantConfig")
public class PlantConfig implements ModelAdapter {

	/**
	 * 植物id
	 */
	public int plantId;
	
	/**
	 * 成熟时间
	 */
	public int growTime;
	
	/**
	 * 成熟后固定奖励
	 * 类型_id_数量公式_几率
	 */
	public String reward;
	
	@FieldIgnore
	public List<RewardConfig> rewardList = new ArrayList<>();;
	
	
	@Override
	public void initialize() {
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for (String[] array : list) {
			RewardConfig reward = new RewardConfig(Arrays.asList(array));
			rewardList.add(reward);
		}
		
		this.reward = null;
	}

}
