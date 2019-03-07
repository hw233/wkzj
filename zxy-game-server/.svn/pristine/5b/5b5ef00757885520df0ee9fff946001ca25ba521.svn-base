package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.model.RandomExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "beastGlobalConfig")
public class BeastGlobalConfig implements ModelAdapter {
	/**
	 * 扣除点券值
	 */
	public String openDate;

	/**
	 * 开放时间
	 */
	public String closeDate;

	/**
	 * 格子开始坐标
	 */
	public int openLv;
	
	/**
	 * 年兽配置id
	 */
	public int monsterConfigId;

	/**
	 *boss刷新时间
	 */
	public String refreshTime;
	
	/*
	 *一堆跑马灯通告 
	 */
	public String beforeStart;
	public String start;
	public String beforeEnd;
	public String beatEnd;
	public String escapeEnd;
	
	/**
	 *通知 boss血量阈值
	 */
	public int beforeEndHp;
	/**
	 * 最后一集奖励
	 */
	private String finalBlowReward;
	
	
	/*8
	 * 攻击必获得物品
	 */
	private String actMustReward;

	/*
	 * 攻击概率获得物品
	 */
	private String actProbReward;

	/*
	 * 攻击保底物品
	 */
	private String actLeastReward;
	
	/*
	 * 攻击保底次数
	 */
	private String actLeastNum;
	
	/*
	 * 攻击必获得物品
	 */
	public int actMustRewardNum;

	/**
	 *能获得掌教经验的次数
	 */
	public int getReputationTimes;
	/**
	 *能获得掌教经验的百分比
	 */
	public int getReputationPrecent;
	/**
	 *boss停留时间
	 */
	public int delayTime;
	
	/**
	 * cd时间
	 */
	public int coolDownTime;
	
	/**
	 * 花费点券数量
	 */
	public int costTicket;
	
	/**
	 * 地图id
	 */
	public int mapId;
	
	/**
	 * 保底max
	 */
	@FieldIgnore
	public int leastMax;

	/**
	 * 保底max
	 */
	@FieldIgnore
	public int leastMin;

	@FieldIgnore
	public List<Integer> timeList = new ArrayList<Integer>();
	
	@FieldIgnore
	private List<Integer> actLeastNumList = new ArrayList<Integer>();
	
	@FieldIgnore
	public List<RewardObject> finalBlowRewardList = new ArrayList<RewardObject>();
	
	@FieldIgnore
	public List<RewardObject> actLeastRewardList = new ArrayList<RewardObject>();

	@FieldIgnore
	public List<Integer> actLeastRewardIdList = new ArrayList<Integer>();

	@FieldIgnore
	public List<ExprRewardObject> actMustRewardList = new ArrayList<ExprRewardObject>();
	
	@FieldIgnore
	public List<RandomExprRewardObject> actProbRewardList = new ArrayList<RandomExprRewardObject>();
	
	@FieldIgnore
	public Date openDateTime = new Date();
	
	@FieldIgnore
	public Date closeDateTime = new Date();
	
	@Override
	public void initialize() {
		openDateTime = DateUtils.string2Date(this.openDate, "yyyy-MM-dd HH:mm:ss");
		closeDateTime = DateUtils.string2Date(this.closeDate, "yyyy-MM-dd HH:mm:ss");
		List<String[]> list = StringUtils.delimiterString2Array(finalBlowReward);
		for (String[] strings : list) {
			finalBlowRewardList.add(RewardObject.valueOf(strings));
		}
		List<String[]> list2 = StringUtils.delimiterString2Array(actLeastReward);
		actLeastRewardIdList.clear();
		for (String[] strings : list2) {
			RewardObject ogObject = RewardObject.valueOf(strings);
			actLeastRewardList.add(ogObject);
			actLeastRewardIdList.add(ogObject.id);
		}
		List<String[]> list1 = StringUtils.delimiterString2Array(actMustReward);
		for (String[] strings : list1) {
			actMustRewardList.add(ExprRewardObject.valueOf(strings));
		}
		List<String[]> list3 = StringUtils.delimiterString2Array(actProbReward);
		for (String[] strings : list3) {
			actProbRewardList.add(RandomExprRewardObject.valueOf(strings));
		}
		
		Collections.unmodifiableList(finalBlowRewardList);
		Collections.unmodifiableList(actMustRewardList);
		
		timeList = StringUtils.delimiterString2IntList(refreshTime, Splitable.ATTRIBUTE_SPLIT);
		actLeastNumList = StringUtils.delimiterString2IntList(actLeastNum, Splitable.ATTRIBUTE_SPLIT);
		leastMin = actLeastNumList.get(0);
		leastMax = actLeastNumList.get(1);
	}

}
