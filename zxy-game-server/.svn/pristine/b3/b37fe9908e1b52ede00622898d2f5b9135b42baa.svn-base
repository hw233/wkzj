package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
@DataFile(fileName = "dailyTasksConfig")
public class DailyTasksConfig implements ModelAdapter {
	/**
	 * 每日任务id
	 */
	private int taskId;
	/**
	 * 每日任务解析器id
	 */
	private int taskType;
	/**
	 * 完成条件
	 */
	private String completeRule;
	/**
	 * 奖励列表
	 */
	private String rewardIds;
	
	@FieldIgnore
	private List<Integer> rewardIdList = new ArrayList<>();
	@Override
	public void initialize() {
		rewardIdList.clear();
		rewardIdList = StringUtils.delimiterString2IntList(rewardIds, Splitable.ATTRIBUTE_SPLIT);
	}
	public int getTaskId() {
		return taskId;
	}
	public int getTaskParser() {
		return taskType;
	}
	public String getCompleteRule() {
		return completeRule;
	}
	public List<Integer> getRewardIdList() {
		return rewardIdList;
	}
	
	
	

}
