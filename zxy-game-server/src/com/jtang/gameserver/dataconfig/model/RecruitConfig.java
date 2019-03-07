package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RandomRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.StringUtils;

/**
 * 聚仙阵配置
 * 
 * @author 0x737263
 * 
 */
@DataFile(fileName = "recruitConfig")
public class RecruitConfig implements ModelAdapter {

	/**
	 * 类型：1小，2：大
	 */
	private int recruitType;

	/**
	 * 免费次数
	 */
	private int freeNum;

	/**
	 * 一次消耗金币
	 */
	private int eachUseGold;
	/**
	 * 一次消耗点券
	 */
	private int eachUseTicket;
	/**
	 * 10连抽消耗金币
	 */
	private int seriesUseGold;
	/**
	 * 10连抽消耗点券
	 */
	private int seriesUseTicket;
	/**
	 * 总冷却时间（分）
	 */
	private int totalInterval;
	
	/**
	 * 产出类型概率，类型(，2：仙人魂魄 ，4：仙人）概率千分比|…
	 */
	private String singleRate;

	/**
	 * 使用物品id
	 */
	private int useGoodsId;

	/**
	 * 使用物品数量
	 */
	private int eachUseGoodsNum;
	
	private int seriesUseGoodsNum;

	/**
	 * 首抽必得
	 */
	private String firstReward;
	/**
	 * 10连抽抽必得
	 */
	private String seriesLeast;
	
	/**
	 * 初始化冷却时间
	 */
	private int initCoolTime;

	@FieldIgnore
	private List<RandomRewardObject> firstList = new ArrayList<>();
	@FieldIgnore
	private List<RandomRewardObject> seriesLeastList = new ArrayList<>();

	@FieldIgnore
	private Map<Integer, Integer> singleDropType = new HashMap<Integer, Integer>();

	@Override
	public void initialize() {
		singleDropType.clear();
		List<String[]> rateList = StringUtils.delimiterString2Array(singleRate);
		for (String[] str : rateList) {
			singleDropType.put(Integer.valueOf(str[0]), Integer.valueOf(str[1]));
		}
		firstList.clear();
		List<String[]> first = StringUtils.delimiterString2Array(firstReward);
		for (String[] fi : first) {
			RandomRewardObject rewardObject = RandomRewardObject.valueOf(fi);
			firstList.add(rewardObject);
		}
		seriesLeastList.clear();
		first = StringUtils.delimiterString2Array(seriesLeast);
		for (String[] fi : first) {
			RandomRewardObject rewardObject = RandomRewardObject.valueOf(fi);
			seriesLeastList.add(rewardObject);
		}

	}

	public Integer getDropType() {
		Integer index = RandomUtils.randomHit(1000, singleDropType);
		if(index == null){
			return null;
		}
		return index;
		
	}

	public int getRecruitType() {
		return recruitType;
	}

	public int getFreeNum() {
		return freeNum;
	}


	public int getEachUseGold() {
		return eachUseGold;
	}

	public int getEachUseTicket() {
		return eachUseTicket;
	}

	public int getSeriesUseGold() {
		return seriesUseGold;
	}

	public int getSeriesUseTicket() {
		return seriesUseTicket;
	}

	/**
	 * 获取总间隔时间
	 * @param value 缩短千分比
	 * @return
	 */
	public int getTotalInterval(int value) {
		if(value == 0){
			return totalInterval;
		} else {
			Double d = Math.ceil(totalInterval * value / 1000.0d);
			return d.intValue();
		}
	}

	public int getUseGoodsId() {
		return useGoodsId;
	}

	public int getEachUseGoodsNum() {
		return eachUseGoodsNum;
	}
	
	public int getSeriesUseGoodsNum() {
		return seriesUseGoodsNum;
	}
	
	public List<RewardObject> getFirstList() {
		Map<Integer, Integer> ids = new HashMap<Integer, Integer>();
		for (int i = 0; i < firstList.size(); i++) {
			RandomRewardObject rewardObject = firstList.get(i);
			ids.put(i, rewardObject.rate);
		}
		
		Integer id = RandomUtils.randomHit(1000, ids);
		if (id == null) {
			return new ArrayList<RewardObject>();
		} else {
			RewardObject result = firstList.get(id);
			result = new RewardObject(result.rewardType, result.id, result.num);
			List<RewardObject> list = new ArrayList<RewardObject>();
			list.add(result);
			return list;
		}
		
	}
	public List<RewardObject> getSeriesLeastList() {
		Map<Integer, Integer> ids = new HashMap<Integer, Integer>();
		for (int i = 0; i < seriesLeastList.size(); i++) {
			RandomRewardObject rewardObject = seriesLeastList.get(i);
			ids.put(i, rewardObject.rate);
		}
		
		Integer id = RandomUtils.randomHit(1000, ids);
		if (id == null) {
			return new ArrayList<RewardObject>();
		} else {
			RewardObject result = seriesLeastList.get(id);
			result = new RewardObject(result.rewardType, result.id, result.num);
			List<RewardObject> list = new ArrayList<RewardObject>();
			list.add(result);
			return list;
		}
		
	}
	
	public int getInitCoolTime() {
		return initCoolTime;
	}


}
