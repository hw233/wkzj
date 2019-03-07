package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "treasureGlobalConfig")
public class TreasureGlobalConfig implements ModelAdapter {

	/**
	 * 免费寻宝次数
	 */
	public int count;

	/**
	 * 扣除点券值
	 */
	public String costTicket;

	/**
	 * 开放时间
	 */
	public String openTime;

	/**
	 * 格子开始坐标
	 */
	public int beginIndex;

	/**
	 * 格子结束坐标
	 */
	public int endIndex;
	
	/**
	 * 每走一步获得奖励
	 */
	public String useNumReward;
	
	/**
	 * 保底步数区间
	 */
	public String leastStep;
	
	/**
	 * 一次迷宫最多走多少步
	 */
	public int maxStep;
	
	/**
	 * 首次使用迷宫走多少步能就能获得大奖
	 */
	public int firstUseStep;

	@FieldIgnore
	private RewardConfig useNumRewardObject = null;
	@FieldIgnore
	private List<Integer> leastList = new ArrayList<>();
	@Override
	public void initialize() {
		List<String> list = StringUtils.delimiterString2List(useNumReward, Splitable.ATTRIBUTE_SPLIT);
		useNumRewardObject = new RewardConfig(list);
		this.useNumReward = null;
		leastList = StringUtils.delimiterString2IntList(leastStep, Splitable.ATTRIBUTE_SPLIT);
		leastStep = null;
	}
	
	public RewardConfig getReward(){
		return useNumRewardObject;
	}

	public int getLeastNum() {
		int min = leastList.get(0);
		int max = leastList.get(1);
		return RandomUtils.nextInt(min, max);
	}

}
